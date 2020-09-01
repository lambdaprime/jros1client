package id.jrosclient.ros.transport;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import id.ICE.MessageResponse;
import id.ICE.MessageServer;
import id.ICE.MessageService;
import id.jrosclient.TopicPublisher;
import id.jrosclient.ros.transport.io.ConnectionHeaderReader;
import id.jrosmessages.MetadataAccessor;
import id.xfunction.function.Unchecked;
import id.xfunction.io.ByteBufferInputStream;
import id.xfunction.logging.XLogger;

/**
 * Allows to communicate with other ROS nodes.
 */
public class TcpRosServer implements MessageService, AutoCloseable {

    private static final XLogger LOGGER = XLogger.getLogger(TcpRosServer.class);
    private static final int DEFAULT_PORT = 1235;
    
    private MetadataAccessor metadataAccessor = new MetadataAccessor();
    private MessageServer server = new MessageServer(this, new ConnectionHeaderScanner())
            .withPort(DEFAULT_PORT);
    private ConnectionHeaderValidator headerValidator = new ConnectionHeaderValidator(
            metadataAccessor);
    private PublishersManager publishersManager;
    private Map<String, TopicPublisherSubscriber> subscribers = new ConcurrentHashMap<>();
    
    public TcpRosServer(PublishersManager publishersManager) {
        this.publishersManager = publishersManager;
    }
    
    public void start() throws IOException {
        LOGGER.fine("Starting...");
        server.run();
    }

    @Override
    public void close() {
        LOGGER.fine("Stopping...");
        Unchecked.run(() ->server.close());
    }

    @Override
    public CompletableFuture<MessageResponse> process(ByteBuffer message) {
        LOGGER.entering("process");
        var dis = new DataInputStream(new ByteBufferInputStream(message));
        var headerReader = new ConnectionHeaderReader(dis);
        var header = Unchecked.get(headerReader::read);
        
        LOGGER.log(Level.FINE, "Incoming connection from {0}", header.getCallerId());
        if (header.getCallerId().isEmpty()) {
            LOGGER.log(Level.FINE, "Caller id is empty, closing...");
            return CompletableFuture.completedFuture(null);
        }
        if (header.getTopic().isEmpty()) {
            LOGGER.log(Level.FINE, "Topic is empty, closing...");
            return CompletableFuture.completedFuture(null);
        }
        var topic = header.getTopic().get();
        var publisherOpt = publishersManager.getPublisher(topic);
        if (publisherOpt.isEmpty()) {
            LOGGER.log(Level.FINE, "No publishers found for topic {0}, closing...", topic);
            return CompletableFuture.completedFuture(null);
        }
        var publisher = publisherOpt.get();

        if (!headerValidator.validate(publisher.getMessageClass(), header)) {
            LOGGER.log(Level.FINE, "Requested message validation error, closing...");
            return CompletableFuture.completedFuture(null);
        }
        return requestMessage(publisher, header.getCallerId().get());
    }

    private CompletableFuture<MessageResponse> requestMessage(
            TopicPublisher<?> publisher, String calledId) 
    {
        var subscriber = subscribers.get(calledId);
        if (subscriber == null) {
            subscriber = new TopicPublisherSubscriber();
            publisher.subscribe(subscriber);
            subscribers.put(calledId, subscriber);
        }
        var future = subscriber.request();
        LOGGER.exiting("process", future);
        return future;
    }

}
