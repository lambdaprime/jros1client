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
import id.jrosmessages.MessageTransformer;
import id.jrosmessages.MetadataAccessor;
import id.xfunction.XAsserts;
import id.xfunction.io.XOutputStream;
import id.xfunction.logging.XLogger;

public class TopicPublisherSubscriber implements Subscriber<Message> {

    private static final XLogger LOGGER = XLogger.getLogger(TcpRosServer.class);
    private MessageTransformer transformer = new MessageTransformer();
    private CompletableFuture<MessageResponse> future = CompletableFuture.completedFuture(null);
    private Subscription subscription;
    private boolean isEstablished;

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
                .withIgnoreNextRequest());
    }

    /**
     * Requests a new message from the publisher to be delivered to subscribed ROS
     * clients.
     * 
     * @return future which completes when new message is published
     */
    public CompletableFuture<MessageResponse> request() {
        XAsserts.assertTrue(future.isDone(),
                "Fail to request new message since previously published message was not processed");
        future = new CompletableFuture<MessageResponse>();
        if (subscription != null)
            subscription.request(1);
        return future;
    }
    
    @Override
    public void onError(Throwable throwable) {
        LOGGER.severe(throwable.getMessage());
    }

    @Override
    public void onComplete() {
        
    }
    
    private MessagePacket createMessagePacket(Message message) {
        MetadataAccessor metadataAccessor = new MetadataAccessor();
        var ch = new ConnectionHeader();
        byte[] body = null;
        if (!isEstablished) {
            ch.withType(metadataAccessor.getType(message.getClass()))
                .withMd5Sum(metadataAccessor.getMd5(message.getClass()));
            isEstablished = true;
        } else {
            body = transformer.transform(message);
        }
        return new MessagePacket(ch, body);
    }
}
