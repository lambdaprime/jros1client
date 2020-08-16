package id.jrosclient.ros.transport.io;

import java.io.DataInput;
import java.io.IOException;

import id.jrosclient.ros.transport.ConnectionHeader;
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
        var ch = readHeader();
        byte[] b = readBody();
        return new MessagePacket(ch, b);
    }

    public ConnectionHeader readHeader() throws IOException {
        return headerReader.read();
    }
    
    public byte[] readBody() throws IOException {
        byte[] b = readBody(utils.readLen(in));
        return b;
    }
    
    private byte[] readBody(int bodyLen) throws IOException {
        byte[] buf = new byte[bodyLen];
        in.readFully(buf);
        return buf;
    }
}
