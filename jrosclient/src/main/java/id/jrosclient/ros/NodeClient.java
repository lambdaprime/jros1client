package id.jrosclient.ros;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.function.Consumer;

import id.jrosclient.ros.transport.ConnectionHeader;
import id.jrosclient.ros.transport.ConnectionHeaderWriter;
import id.jrosclient.ros.transport.MessagePacket;
import id.jrosclient.ros.transport.MessagePacketReader;

/**
 * Allows to communicate with other ROS nodes.
 */
public class NodeClient {

    private DataOutputStream dos;
    private DataInputStream dis;
    private Consumer<MessagePacket> handler;
    private ConnectionHeaderWriter writer;
    private MessagePacketReader reader;

    private NodeClient(SocketChannel channel, Consumer<MessagePacket> handler) {
        OutputStream os = Channels.newOutputStream(channel);
        dis = new DataInputStream(Channels.newInputStream(channel));
        dos = new DataOutputStream(new BufferedOutputStream(os));
        writer = new ConnectionHeaderWriter(dos);
        reader = new MessagePacketReader(dis);
        this.handler = handler;
    }

    public static NodeClient connect(String host, int port, Consumer<MessagePacket> handler)
            throws IOException {
        SocketChannel channel = SocketChannel.open(new InetSocketAddress(host, port));
        return new NodeClient(channel, handler);
    }

    public void start(ConnectionHeader header) throws IOException {
        writer.write(header);
        dos.flush();
        MessagePacket response = null;
        while ((response = reader.read()) != null) {
            handler.accept(response);
        }
    }

}
