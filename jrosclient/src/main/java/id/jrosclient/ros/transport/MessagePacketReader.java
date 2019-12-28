package id.jrosclient.ros.transport;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

public class MessagePacketReader {

    private DataInput in;
    private ConnectionHeaderReader headerReader;

    public MessagePacketReader(DataInputStream in) {
        this.in = in;
        headerReader = new ConnectionHeaderReader(in);
    }

    public MessagePacket read() throws IOException {
        var ch = headerReader.read();
        return new MessagePacket(ch);
    }

}
