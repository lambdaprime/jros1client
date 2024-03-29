/*
 * Copyright 2020 jrosclient project
 * 
 * Website: https://github.com/lambdaprime/jros1client
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
package id.jros1client.ros.transport;

import id.ICE.MessageRequest;
import id.ICE.MessageResponse;
import id.ICE.MessageServer;
import id.ICE.MessageService;
import id.jros1client.JRos1ClientConfiguration;
import id.jros1client.ros.transport.io.DefaultConnectionHeaderReader;
import id.jrosclient.utils.TextUtils;
import id.jrosmessages.MessageMetadataAccessor;
import id.xfunction.Preconditions;
import id.xfunction.function.Unchecked;
import id.xfunction.io.ByteBufferInputStream;
import id.xfunction.lang.XRE;
import id.xfunction.logging.TracingToken;
import id.xfunction.logging.XLogger;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Flow.Subscriber;
import java.util.logging.Level;

/**
 * Allows to communicate with other ROS nodes.
 *
 * <p>Relies on {@link PublishersManager} for list of available publishers. - When new connection
 * comes in it searches for publisher in PublishersManager - Each time existing client sends a
 * request it checks if publisher is closed or not (present in PublishersManager) and if not then it
 * closes the connection.
 *
 * @author lambdaprime intid@protonmail.com
 */
public class TcpRosServer implements MessageService, AutoCloseable {

    private XLogger logger;

    private MessageMetadataAccessor metadataAccessor = new MessageMetadataAccessor();
    private MessageServer server;
    private ConnectionHeaderValidator headerValidator =
            new ConnectionHeaderValidator(metadataAccessor);
    private PublishersManager publishersManager;

    /** Connection id to subscriber serving it */
    private Map<Integer, TopicPublisherSubscriber> subscribers = new ConcurrentHashMap<>();

    /** Connections which are no longer served by publishers and should be closed */
    private Set<Integer> closedConnections = new HashSet<>();

    private TextUtils utils;
    private boolean isStarted;

    private TracingToken tracingToken;

    @SuppressWarnings("resource")
    public TcpRosServer(
            @SuppressWarnings("exports") TracingToken tracingToken,
            PublishersManager publishersManager,
            JRos1ClientConfiguration config,
            TextUtils utils) {
        this.tracingToken = tracingToken;
        this.publishersManager = publishersManager;
        server =
                new MessageServer(this, new ConnectionHeaderScanner())
                        .withPort(config.getTcpRosServerPort());
        this.utils = utils;
        logger =
                XLogger.getLogger(
                        TcpRosServer.class, new TracingToken(tracingToken, "" + hashCode()));
    }

    public void start() throws IOException {
        if (isStarted) return;
        logger.fine("Starting...");
        isStarted = true;
        server.run();
    }

    @Override
    public void close() {
        var publishers = publishersManager.getPublishers();
        if (!publishers.isEmpty()) {
            throw new XRE("Attempt to close client with active publishers: %s", publishers);
        }
        logger.fine("Stopping...");
        subscribers.values().forEach(Subscriber::onComplete);
        subscribers.clear();
        Unchecked.run(() -> server.close());
        isStarted = false;
    }

    /** Implementation of {@link MessageService} which process the incoming requests. */
    @SuppressWarnings("exports")
    @Override
    public CompletableFuture<MessageResponse> process(MessageRequest request) {
        var connId = request.getConnectionId();
        logger.entering("process", connId);

        if (closedConnections.remove(connId)) {
            logger.info("Closing connection as there is no more publishers serving it...");
            return CompletableFuture.completedFuture(null);
        }

        var subscriber = subscribers.get(connId);

        // if there is no subscriber for that connection, we try to create one
        if (subscriber == null) {
            var message = request.getMessage().orElse(null);
            if (message == null) {
                logger.info(
                        "Received registration request with no message, closing the connection...");
                return CompletableFuture.completedFuture(null);
            }
            var subscriberOpt = registerSubscriber(connId, message);
            if (subscriberOpt.isEmpty()) {
                return CompletableFuture.completedFuture(null);
            }
            subscriber = subscriberOpt.get();
            subscribers.put(connId, subscriber);
        } else {
            var topic = subscriber.getTopic();
            var publisherPresent = publishersManager.getPublisher(topic).isPresent();
            if (!publisherPresent) {
                logger.log(Level.FINE, "No publishers found for topic {0}, closing...", topic);
                subscribers.remove(connId);
                subscriber.onComplete();
                return CompletableFuture.completedFuture(null);
            }
        }

        Preconditions.isTrue(!subscriber.isCompleted());

        var callerId = subscriber.getCallerId();
        logger.log(Level.FINE, "Requesting next message for {0}", callerId);

        var future = subscriber.request();

        logger.exiting("process", future);
        return future;
    }

    /**
     * Find publisher for the topic for which this request is for. If such publisher exist - create
     * a subscriber for it and return this subscriber. If not - return empty value.
     */
    private Optional<TopicPublisherSubscriber> registerSubscriber(int connId, ByteBuffer message) {
        var dis = new DataInputStream(new ByteBufferInputStream(message));
        var headerReader = new DefaultConnectionHeaderReader(dis);
        var header = Unchecked.get(headerReader::read);

        logger.log(Level.FINE, "Incoming connection from {0}", header.getCallerId());
        if (header.getCallerId().isEmpty()) {
            logger.log(Level.FINE, "Caller id is empty, closing...");
            return Optional.empty();
        }
        if (header.getTopic().isEmpty()) {
            logger.log(Level.FINE, "Topic is empty, closing...");
            return Optional.empty();
        }

        var callerId = header.getCallerId().get();
        var topic = header.getTopic().get();

        var publisherOpt = publishersManager.getPublisher(topic);
        if (publisherOpt.isEmpty()) {
            logger.log(Level.FINE, "No publishers found for topic {0}, closing...", topic);
            return Optional.empty();
        }
        var publisher = publisherOpt.get();

        if (!headerValidator.validate(publisher.getMessageClass(), header)) {
            logger.log(Level.FINE, "Requested message validation error, closing...");
            return Optional.empty();
        }

        var subscriber =
                new TopicPublisherSubscriber(
                        tracingToken, callerId, topic, publisher.getMessageClass(), utils) {
                    @Override
                    public void onError(Throwable throwable) {
                        logger.warning(
                                "Publisher of topic {0} for caller {1} throwed error {2}: {3}",
                                topic, callerId, throwable.getClass(), throwable.getMessage());

                        subscribers.remove(connId);
                        publisher.onPublishError(throwable);
                        closedConnections.add(connId);
                        super.onError(throwable);
                    }
                };
        publisher.subscribe(subscriber);

        logger.log(Level.FINE, "Received connection header {0}", header);
        return Optional.of(subscriber);
    }

    public boolean isClosed() {
        return !isStarted;
    }
}
