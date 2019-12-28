package id.jrosclient.ros.transport;

public class MessagePacket {

    private ConnectionHeader header;
    private byte[] body;

    public MessagePacket(ConnectionHeader header) {
        this(header, new byte[0]);
    }

    public MessagePacket(ConnectionHeader header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public ConnectionHeader getHeader() {
        return header;
    }
}
