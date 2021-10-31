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
 * - lambdaprime <intid@protonmail.com>
 */
package id.jrosclient.ros.transport;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import id.ICE.MessageResponse;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosclient.impl.TextUtils;
import id.jrosclient.ros.transport.io.MessagePacketWriter;
import id.jrosmessages.Message;
import id.jrosmessages.impl.MessageTransformer;
import id.jrosmessages.impl.MetadataAccessor;
import id.xfunction.XAsserts;
import id.xfunction.io.XOutputStream;
import id.xfunction.lang.XRE;
import id.xfunction.logging.XLogger;

/**
 * Each instance serves a single remote ROS client which is subscribed to some
 * particular topic published by current instance of jrosclient.
 * 
 * This class is subscribed to jrosclient publishers {@link TopicSubmissionPublisher}
 * which are created by users to publish messages to certain ROS topics.
 */
public class TopicPublisherSubscriber implements Subscriber<Message> {

    private static final XLogger LOGGER = XLogger.getLogger(TcpRosServer.class);
    private TextUtils utils;
    private MetadataAccessor metadataAccessor = new MetadataAccessor();
    private MessageTransformer transformer = new MessageTransformer();
    private CompletableFuture<MessageResponse> future = CompletableFuture.completedFuture(null);
    
    /**
     * Since onSubscribe is called async (by SubmissionPublisher) it may happen that it
     * will be called after next message will be requested by request().
     * To make sure that we will not lose the request() call we will block it until
     * we will not subscribe.
     */
    private CompletableFuture<Subscription> subscriptionFuture = new CompletableFuture<Subscription>();
    private boolean isConnectionEstablished;
    private boolean isCompleted;
    private String topic;
    private String callerId;
    private Class<? extends Message> messageClass;

    public TopicPublisherSubscriber(String callerId, String topic, Class<? extends Message> messageClass,
            TextUtils utils) {
        this.callerId = callerId;
        this.topic = topic;
        this.messageClass = messageClass;
        this.utils = utils;
    }
    
    @Override
    public void onSubscribe(Subscription subscription) {
        subscriptionFuture.complete(subscription);
    }

    @Override
    public void onNext(Message message) {
        LOGGER.entering("onNext");
        LOGGER.fine("Published new message: {0}", utils.toString(message));
        XAsserts.assertTrue(message.getClass() == messageClass, "Incompatible message type");
        var packet = createMessagePacket(message);
        sendPacket(packet);
        LOGGER.exiting("onNext");
    }

    /**
     * Requests a new message from the publisher to be delivered to subscribed ROS
     * clients.
     * 
     * This method is called when we receive a new request from ROS subscriber which
     * means that it is ready for next message available in the topic.
     * 
     * @return future which completes when new message is published
     */
    public CompletableFuture<MessageResponse> request() {
        XAsserts.assertTrue(!isCompleted,
                "Fail to request new message since subscriber has completed");
        if (!future.isDone())
            return future;
        future = new CompletableFuture<MessageResponse>();
        Subscription subscription;
        try {
            subscription = subscriptionFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new XRE("Never was subscribed to the TopicPublisher");
        }
        if (isConnectionEstablished) {
            subscription.request(1);
        } else {
            LOGGER.fine("This is a new connection - creating handshake packet first");
            var packet = createHandshakeMessagePacket();
            sendPacket(packet);
            isConnectionEstablished = true;
        }
        return future;
    }
    
    @Override
    public void onError(Throwable throwable) {
        LOGGER.entering("onError");
        LOGGER.severe(throwable.getMessage());
        subscriptionFuture.complete(null);
        try {
            subscriptionFuture.get().cancel();
        } catch (InterruptedException | ExecutionException e) {
            throwable.addSuppressed(e);
        }
        isCompleted = true;
        future.complete(null);
        LOGGER.exiting("onError");
    }

    @Override
    public void onComplete() {
        LOGGER.entering("onComplete");
        isCompleted = true;
        LOGGER.exiting("onComplete");
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

    private void sendPacket(MessagePacket packet) {
        LOGGER.entering("sendPacket");
        var os = new XOutputStream();
        var dos = new DataOutputStream(new BufferedOutputStream(os));
        var writer = new MessagePacketWriter(dos);
        try {
            writer.write(packet);
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LOGGER.fine("Sending packet to subscriber");
        LOGGER.fine(utils.toString(os.asHexString()));
        // tell ICE that it can send the response back
        future.complete(new MessageResponse(ByteBuffer.wrap(os.toByteArray()))
                .withIgnoreNextRequest()
                .withErrorHandler(this::onError));
        LOGGER.exiting("sendPacket");
    }

    private MessagePacket createHandshakeMessagePacket() {
        var ch = new ConnectionHeader();
        ch.withType(metadataAccessor.getType(messageClass))
            .withMd5Sum(metadataAccessor.getMd5(messageClass));
        return new MessagePacket(ch, null);
    }

    private MessagePacket createMessagePacket(Message message) {
        var ch = new ConnectionHeader();
        byte[] body = transformer.transform(message);
        return new MessagePacket(ch, body);
    }
}
