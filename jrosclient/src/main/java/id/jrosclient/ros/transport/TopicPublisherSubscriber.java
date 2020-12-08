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

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import id.ICE.MessageResponse;
import id.jrosclient.ros.transport.io.MessagePacketWriter;
import id.jrosmessages.Message;
import id.jrosmessages.impl.MessageTransformer;
import id.jrosmessages.impl.MetadataAccessor;
import id.xfunction.XAsserts;
import id.xfunction.io.XOutputStream;
import id.xfunction.logging.XLogger;

public class TopicPublisherSubscriber implements Subscriber<Message> {

    private static final XLogger LOGGER = XLogger.getLogger(TcpRosServer.class);
    private MessageTransformer transformer = new MessageTransformer();
    private CompletableFuture<MessageResponse> future = CompletableFuture.completedFuture(null);
    private Subscription subscription;
    private boolean isConnectionEstablished;
    private boolean isCompleted;
    private String topic;
    private String callerId;

    public TopicPublisherSubscriber(String callerId, String topic) {
        this.callerId = callerId;
        this.topic = topic;
    }
    
    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(Message message) {
        LOGGER.fine("Published new message: {0}", message);
        var os = new XOutputStream();
        var dos = new DataOutputStream(new BufferedOutputStream(os));
        var writer = new MessagePacketWriter(dos);
        var packet = createMessagePacket(message);
        try {
            writer.write(packet);
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LOGGER.fine("Sending message to subscriber");
        LOGGER.fine(os.asHexString());
        future.complete(new MessageResponse(ByteBuffer.wrap(os.toByteArray()))
                .withIgnoreNextRequest()
                .withErrorHandler(this::onError));
    }

    /**
     * Requests a new message from the publisher to be delivered to subscribed ROS
     * clients.
     * 
     * @return future which completes when new message is published
     */
    public CompletableFuture<MessageResponse> request() {
        XAsserts.assertTrue(!isCompleted,
                "Fail to request new message since subscriber is completed");
        if (!future.isDone())
            return future;
        future = new CompletableFuture<MessageResponse>();
        if (subscription != null)
            subscription.request(1);
        return future;
    }
    
    @Override
    public void onError(Throwable throwable) {
        LOGGER.severe(throwable.getMessage());
        subscription.cancel();
        isCompleted = true;
    }

    @Override
    public void onComplete() {
        isCompleted = true;
    }
    
    public boolean isCompleted() {
        return isCompleted;
    }
    
    public String getTopic() {
        return topic;
    }

    public String getCallerId() {
        return callerId;
    }
    
    private MessagePacket createMessagePacket(Message message) {
        MetadataAccessor metadataAccessor = new MetadataAccessor();
        var ch = new ConnectionHeader();
        byte[] body = null;
        if (!isConnectionEstablished) {
            ch.withType(metadataAccessor.getType(message.getClass()))
                .withMd5Sum(metadataAccessor.getMd5(message.getClass()));
            isConnectionEstablished = true;
        } else {
            body = transformer.transform(message);
        }
        return new MessagePacket(ch, body);
    }
}
