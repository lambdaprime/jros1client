package id.jrosmessages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import id.kineticstreamer.KineticStreamReader;
import id.kineticstreamer.KineticStreamWriter;

public class MessageTransformer {

    public <M extends Message> M transform(byte[] data, Class<M> clazz) {
        try {
            var dis = new DataInputStream(new ByteArrayInputStream(data));
            var ks = new KineticStreamReader(new RosDataInput(dis));
            Object obj = ks.read(clazz);
            return (M) obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] transform(Message message) {
        try {
            var bos = new ByteArrayOutputStream();
            var dos = new DataOutputStream(bos);
            var ks = new KineticStreamWriter(new RosDataOutput(dos));
            ks.write(message);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
