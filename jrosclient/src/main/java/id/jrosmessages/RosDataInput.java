package id.jrosmessages;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Array;

import id.kineticstreamer.InputKineticStream;
import id.kineticstreamer.KineticStreamReader;

public class RosDataInput implements InputKineticStream {

    private DataInput in;

    public RosDataInput(DataInput in) {
        this.in = in;
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
