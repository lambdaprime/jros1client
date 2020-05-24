package id.jrosclient.ros.transport;

import java.io.DataInput;
import java.io.IOException;

import id.jrosmessages.RosDataInput;

public class MessagePacketReader {

    private RosDataInput in;
    private ConnectionHeaderReader headerReader;

    public MessagePacketReader(DataInput input) {
        this.in = new RosDataInput(input);
        headerReader = new ConnectionHeaderReader(in);
    }

    public MessagePacket read() throws IOException {
        var ch = headerReader.read();
        byte[] b = in.readBody(in.readLen());
        return new MessagePacket(ch, b);
    }

}
