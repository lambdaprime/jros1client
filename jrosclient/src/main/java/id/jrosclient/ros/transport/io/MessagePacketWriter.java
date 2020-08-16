package id.jrosclient.ros.transport.io;

import java.io.DataOutput;
import java.io.IOException;

import id.jrosclient.ros.transport.MessagePacket;

public class MessagePacketWriter {

    private DataOutput out;
    private ConnectionHeaderWriter headerWriter;
    private Utils utils = new Utils();
    
    public MessagePacketWriter(DataOutput output) {
        this.out = output;
        headerWriter = new ConnectionHeaderWriter(output);
    }

    public void write(MessagePacket packet) throws IOException {
        headerWriter.write(packet.getHeader());
        var body = packet.getBody();
        utils.writeLen(out, body.length);
        out.write(body);
    }

}
