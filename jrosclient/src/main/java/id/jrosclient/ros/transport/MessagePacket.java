package id.jrosclient.ros.transport;

import java.io.ByteArrayInputStream;

import id.kineticstreamer.OutputStreamByteList;
import id.xfunction.function.Unchecked;

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

    public byte[] getBody() {
        return body;
    }
    
    @Override
    public String toString() {
        var out = new OutputStreamByteList();
        Unchecked.runUnchecked(() -> new ByteArrayInputStream(body).transferTo(out));
        return String.format("{ header: %s, body: [%s]}", header, out.asHexString());
    }
}
