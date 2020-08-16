package id.jrosclient.ros.transport;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Level;
import java.util.logging.Logger;

import id.jrosclient.ros.transport.io.ConnectionHeaderWriter;
import id.jrosclient.ros.transport.io.MessagePacketReader;
import id.jrosmessages.Message;
import id.jrosmessages.MessageTransformer;
import id.jrosmessages.MetadataAccessor;
import id.xfunction.XUtils;
import id.xfunction.concurrent.SameThreadExecutorService;
import id.xfunction.logging.XLogger;

/**
 * Allows to communicate with other ROS nodes.
 */
public class TcpRosClient<M extends Message> extends SubmissionPublisher<M> implements AutoCloseable {

    private static final Logger LOGGER = XLogger.getLogger(TcpRosClient.class);
    
    private String callerId;
    private String topic;
    private String host;
    private int port;
    private Class<M> messageClass;
    private DataOutputStream dos;
    private DataInputStream dis;
    private ConnectionHeaderWriter writer;
    private MessagePacketReader reader;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TcpRosClient(String callerId, String topic, String host, int port,
            Class<M> messageClass) {
        super(new SameThreadExecutorService(), 1);
        this.callerId = callerId;
        this.topic = topic;
        this.host = host;
        this.port = port;
        this.messageClass = messageClass;
    }
    
    public void connect() throws IOException {
        SocketChannel channel = SocketChannel.open(new InetSocketAddress(host, port));
        OutputStream os = Channels.newOutputStream(channel);
        dis = new DataInputStream(Channels.newInputStream(channel));
        dos = new DataOutputStream(new BufferedOutputStream(os));
        writer = new ConnectionHeaderWriter(dos);
        reader = new MessagePacketReader(dis);
        MetadataAccessor metadataAccessor = new MetadataAccessor();
        String messageDefinition = "string data";
        var ch = new ConnectionHeader()
                .withTopic("/" + topic)
                .withCallerId(callerId)
                .withType(metadataAccessor.getType(messageClass))
                .withMessageDefinition(messageDefinition )
                .withMd5Sum(metadataAccessor.getMd5(messageClass));
        executorService.execute(() -> {
            try {
                run(ch);
            } catch (Exception e) {
                XUtils.printExceptions(e);
            } finally {
                executorService.shutdown();
            }
        });
    }

    private void run(ConnectionHeader header) throws Exception {
        while (!executorService.isShutdown()) {
            writer.write(header);
            dos.flush();
            MessagePacket response = reader.read();
            LOGGER.log(Level.FINE, "Message packet: {0}", response);
            var msg = new MessageTransformer().transform(response.getBody(), messageClass);
            submit(msg);
        }
    }

    @Override
    public void close() {
        super.close();
        executorService.shutdown();
    }
}
