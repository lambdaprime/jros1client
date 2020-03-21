package id.jrosclient.ros;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import id.jrosclient.ros.transport.ConnectionHeader;
import id.jrosclient.ros.transport.ConnectionHeaderWriter;
import id.jrosclient.ros.transport.MessagePacket;
import id.jrosclient.ros.transport.MessagePacketReader;
import id.xfunction.function.Curry;
import id.xfunction.function.Unchecked;

/**
 * Allows to communicate with other ROS nodes.
 */
public class NodeClient implements AutoCloseable {

    private DataOutputStream dos;
    private DataInputStream dis;
    private volatile Consumer<MessagePacket> handler = mp -> {};
    private ConnectionHeaderWriter writer;
    private MessagePacketReader reader;
    private Optional<ExecutorService> executorService = Optional.empty();

    private NodeClient(SocketChannel channel) {
        OutputStream os = Channels.newOutputStream(channel);
        dis = new DataInputStream(Channels.newInputStream(channel));
        dos = new DataOutputStream(new BufferedOutputStream(os));
        writer = new ConnectionHeaderWriter(dos);
        reader = new MessagePacketReader(dis);
    }

    public static NodeClient connect(String host, int port)
            throws IOException {
        SocketChannel channel = SocketChannel.open(new InetSocketAddress(host, port));
        return new NodeClient(channel);
    }

    public void setHandler(Consumer<MessagePacket> handler) {
        this.handler = handler;
    }

    public void start(ConnectionHeader header) throws Exception {
        if (executorService.isPresent())
            return;
        executorService = Optional.of(Executors.newSingleThreadExecutor());
        executorService.get().execute(() -> Unchecked.runUnchecked(
                Curry.curry(this::run, header)));
    }

    private void run(ConnectionHeader header) throws Exception {
        writer.write(header);
        dos.flush();
        MessagePacket response = null;
        while ((response = reader.read()) != null) {
            handler.accept(response);
            if (executorService.get().isShutdown()) break;
        }
    }

    @Override
    public void close() throws Exception {
        executorService.ifPresent(Unchecked.wrapAccept(es -> {
            es.shutdown();
        }));
    }
}
