package id.jrosclient.ros.transport.io;

import java.io.DataInput;
import java.io.IOException;

import id.jrosclient.ros.transport.MessagePacket;

public class MessagePacketReader {

    private DataInput in;
    private ConnectionHeaderReader headerReader;
    private Utils utils = new Utils();

    public MessagePacketReader(DataInput input) {
        this.in = input;
        headerReader = new ConnectionHeaderReader(in);
    }

    public MessagePacket read() throws IOException {
        var ch = headerReader.read();
        byte[] b = readBody(utils.readLen(in));
        return new MessagePacket(ch, b);
    }

    private byte[] readBody(int bodyLen) throws IOException {
        byte[] buf = new byte[bodyLen];
        in.readFully(buf);
        return buf;
    }
}
