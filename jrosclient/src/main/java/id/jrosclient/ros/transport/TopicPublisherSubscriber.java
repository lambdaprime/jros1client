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
import id.xfunction.io.XOutputStream;
import id.xfunction.logging.XLogger;

public class TopicPublisherSubscriber implements Subscriber<Message> {

    private static final XLogger LOGGER = XLogger.getLogger(TcpRosServer.class);
    private MessageTransformer transformer = new MessageTransformer();
    private CompletableFuture<MessageResponse> future;
    private Subscription subscription;

    public TopicPublisherSubscriber(CompletableFuture<MessageResponse> future) {
        this.future = future;
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
        MetadataAccessor metadataAccessor = new MetadataAccessor();
        var ch = new ConnectionHeader()
                .withType(metadataAccessor.getType(message.getClass()))
                .withMd5Sum(metadataAccessor.getMd5(message.getClass()));
        try {
            writer.write(new MessagePacket(ch, transformer.transform(message)));
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LOGGER.fine("Sending message to subscriber");
        LOGGER.fine(os.asHexString());
        future.complete(new MessageResponse(ByteBuffer.wrap(os.toByteArray()), false));
        //subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        LOGGER.severe(throwable.getMessage());
    }

    @Override
    public void onComplete() {
        
    }

}
