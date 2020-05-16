package id.jrosclient.ros.transport;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import id.kineticstreamer.KineticStreamReader;

public class MessageTransformer {

    public <M> M transform(byte[] data, Class<M> clazz) {
        try {
            var dis = new DataInputStream(new ByteArrayInputStream(data));
            Object obj = clazz.getConstructor().newInstance();
            KineticStreamReader ks = new KineticStreamReader(new RosDataInput(dis));
            ks.read(obj);
            return (M) obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
