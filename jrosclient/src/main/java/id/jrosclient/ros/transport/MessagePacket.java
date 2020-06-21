package id.jrosclient.ros.transport;

import java.io.ByteArrayInputStream;

import id.xfunction.function.Unchecked;
import id.xfunction.io.XOutputStream;

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
        var out = new XOutputStream();
        Unchecked.run(() -> new ByteArrayInputStream(body).transferTo(out));
        return String.format("{ header: %s, body: [%s]}", header, out.asHexString());
    }
}
