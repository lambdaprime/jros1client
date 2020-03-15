package id.jrosclient.ros.transport;

import java.io.DataInput;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import id.kineticstreamer.KineticDataInput;

public class RosDataInput implements KineticDataInput {

    private DataInput in;

    public RosDataInput(DataInput in) {
        this.in = in;
    }

    public int readLen() throws IOException {
        return Integer.reverseBytes(in.readInt());
    }

    public Entry<String, String> readField(int fieldLen) throws IOException {
        byte[] buf = new byte[fieldLen];
        in.readFully(buf);
        var a = new String(buf).split("=");
        return Map.entry(a[0], a[1]);
    }

    public byte[] readBody(int bodyLen) throws IOException {
        byte[] buf = new byte[bodyLen];
        in.readFully(buf);
        return buf;
    }

    @Override
    public int readInt() throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String readString() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int readeLen() throws IOException {
        // TODO Auto-generated method stub
        return 0;
    }
}
