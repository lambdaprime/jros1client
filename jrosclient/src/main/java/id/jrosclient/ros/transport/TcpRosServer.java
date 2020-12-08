/*
 * Copyright 2020 jrosclient project
 * 
 * Website: https://github.com/lambdaprime/jrosclient
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Authors:
 * - lambdaprime <id.blackmesa@gmail.com>
 */
package id.jrosclient.ros.transport;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Flow.Subscriber;
import java.util.logging.Level;

import id.ICE.MessageRequest;
import id.ICE.MessageResponse;
import id.ICE.MessageServer;
import id.ICE.MessageService;
import id.jrosclient.JRosClientConfiguration;
import id.jrosclient.ros.transport.io.ConnectionHeaderReader;
import id.jrosmessages.impl.MetadataAccessor;
import id.xfunction.XAsserts;
import id.xfunction.XRE;
import id.xfunction.function.Unchecked;
import id.xfunction.io.ByteBufferInputStream;
import id.xfunction.logging.XLogger;

/*
 * Allows to communicate with other ROS nodes.
 * 
 * Relies on PublishersManager for list of available publishers.
 * - When new connection comes in it searches for publisher
 * in PublishersManager
 * - Each time existing client sends a request it checks if
 * publisher is closed or not (present in PublishersManager) and
 * if not then it closes the connection.
 */
public class TcpRosServer implements MessageService, AutoCloseable {

    private static final XLogger LOGGER = XLogger.getLogger(TcpRosServer.class);
    
    private MetadataAccessor metadataAccessor = new MetadataAccessor();
    private MessageServer server;
    private ConnectionHeaderValidator headerValidator = new ConnectionHeaderValidator(
            metadataAccessor);
    private PublishersManager publishersManager;
    // connection id to subscriber serving it
    private Map<Integer, TopicPublisherSubscriber> subscribers = new ConcurrentHashMap<>();
    
    private boolean isStarted;
    
    public TcpRosServer(PublishersManager publishersManager, JRosClientConfiguration config) {
        this.publishersManager = publishersManager;
        server = new MessageServer(this, new ConnectionHeaderScanner())
                .withPort(config.getTcpRosServerPort());
    }
    
    public void start() throws IOException {
        if (isStarted) return;
        LOGGER.fine("Starting...");
        isStarted = true;
        server.run();
    }

    @Override
    public void close() {
        var publishers = publishersManager.getPublishers();
        if (!publishers.isEmpty()) {
            throw new XRE("Attempt to close client with active publishers: %s", publishers);
        }
        LOGGER.fine("Stopping...");
        subscribers.values()
            .forEach(Subscriber::onComplete);
        subscribers.clear();
        Unchecked.run(() -> server.close());
        isStarted = false;
    }

    @Override
    public CompletableFuture<MessageResponse> process(MessageRequest request) {
        LOGGER.entering("process");
        var connId = request.getConnectionId();
        
        var subscriber = subscribers.get(connId);
        if (subscriber == null) {
            var subscriberOpt = registerSubscriber(request);
            if (subscriberOpt.isEmpty()) {
                return CompletableFuture.completedFuture(null);
            }
            subscriber = subscriberOpt.get();
            subscribers.put(connId, subscriber);
        } else {
            var topic = subscriber.getTopic();
            var publisherPresent = publishersManager.getPublisher(topic).isPresent();
            if (!publisherPresent) {
                LOGGER.log(Level.FINE, "No publishers found for topic {0}, closing...", topic);
                subscribers.remove(connId);
                subscriber.onComplete();
                return CompletableFuture.completedFuture(null);
            }
        }

        XAsserts.assertTrue(!subscriber.isCompleted());
        
        var callerId = subscriber.getCallerId();
        LOGGER.log(Level.FINE, "Requesting next message for {0}", callerId);

        var future = subscriber.request();
        
        LOGGER.exiting("process", future);
        return future;
    }

    private Optional<TopicPublisherSubscriber> registerSubscriber(MessageRequest request) {
        var message = request.getMessage().orElseThrow(() ->
        new XRE("Incoming request has no message"));
        var dis = new DataInputStream(new ByteBufferInputStream(message));
        var headerReader = new ConnectionHeaderReader(dis);
        var header = Unchecked.get(headerReader::read);
        
        LOGGER.log(Level.FINE, "Incoming connection from {0}", header.getCallerId());
        if (header.getCallerId().isEmpty()) {
            LOGGER.log(Level.FINE, "Caller id is empty, closing...");
            return Optional.empty();
        }
        if (header.getTopic().isEmpty()) {
            LOGGER.log(Level.FINE, "Topic is empty, closing...");
            return Optional.empty();
        }
        
        var callerId = header.getCallerId().get();
        var topic = header.getTopic().get();
        
        var publisherOpt = publishersManager.getPublisher(topic);
        if (publisherOpt.isEmpty()) {
            LOGGER.log(Level.FINE, "No publishers found for topic {0}, closing...", topic);
            return Optional.empty();
        }
        var publisher = publisherOpt.get();

        if (!headerValidator.validate(publisher.getMessageClass(), header)) {
            LOGGER.log(Level.FINE, "Requested message validation error, closing...");
            return Optional.empty();
        }
        
        var subscriber = new TopicPublisherSubscriber(callerId, topic) {
            @Override
            public void onError(Throwable throwable) {
                LOGGER.log(Level.WARNING, "Failed to deliver message to ROS subscriber {0} due to: {1}",
                        new Object[] {callerId, throwable.getMessage()});
                subscribers.remove(request.getConnectionId());
                super.onError(throwable);
            }
        };
        publisher.subscribe(subscriber);
        
        LOGGER.log(Level.FINE, "Received connection header {0}", header);
        return Optional.of(subscriber);
    }
}
