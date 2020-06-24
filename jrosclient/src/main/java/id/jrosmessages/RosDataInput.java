package id.jrosmessages;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Map.Entry;

import id.kineticstreamer.InputKineticStream;
import id.kineticstreamer.KineticStreamReader;

public class RosDataInput implements InputKineticStream {

    private DataInput in;

    public RosDataInput(DataInput in) {
        this.in = in;
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
        return Integer.reverseBytes(in.readInt());
    }

    @Override
    public String readString() throws IOException {
        int len = readLen();
        byte[] b = new byte[len];
        in.readFully(b);
        return new String(b);
    }

    public int readLen() throws IOException {
        return Integer.reverseBytes(in.readInt());
    }

    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(
                Long.reverseBytes(
                        Double.doubleToRawLongBits(in.readDouble())));
    }

    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(
                Integer.reverseBytes(
                        Float.floatToRawIntBits(in.readFloat())));
    }

    @Override
    public boolean readBool() throws IOException {
        return in.readBoolean();
    }

    @Override
    public Object[] readArray(Class<?> type) throws Exception {
        var array = (Object[])Array.newInstance(type, readLen());
        for (int i = 0; i < array.length; i++) {
            array[i] = new KineticStreamReader(this).read(type);
        }
        return array;
    }
}
