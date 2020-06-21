package id.jrosmessages;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import id.kineticstreamer.KineticStreamReader;

public class MessageTransformer {

    public <M> M transform(byte[] data, Class<M> clazz) {
        try {
            var dis = new DataInputStream(new ByteArrayInputStream(data));
            KineticStreamReader ks = new KineticStreamReader(new RosDataInput(dis));
            Object obj = ks.read(clazz);
            return (M) obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
