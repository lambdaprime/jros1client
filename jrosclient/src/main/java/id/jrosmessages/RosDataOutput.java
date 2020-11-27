package id.jrosmessages;

import java.io.DataOutput;
import java.io.IOException;

import id.kineticstreamer.KineticStreamWriter;
import id.kineticstreamer.OutputKineticStream;

public class RosDataOutput implements OutputKineticStream {

    private DataOutput out;

    public RosDataOutput(DataOutput out) {
        this.out = out;
    }

    public void writeLen(int len) throws IOException {
        out.writeInt(Integer.reverseBytes(len));
    }
    
    @Override
    public void writeString(String str) throws IOException {
        writeLen(str.length());
        out.write(str.getBytes());
    }

    @Override
    public void writeInt(Integer i) throws IOException {
        out.writeInt(Integer.reverseBytes(i));
    }

    @Override
    public void writeDouble(Double f) throws IOException {
        out.writeDouble(Double.longBitsToDouble(
                Long.reverseBytes(
                        Double.doubleToRawLongBits(f))));
    }

    @Override
    public void writeFloat(Float f) throws IOException {
        out.writeFloat(Float.intBitsToFloat(
                Integer.reverseBytes(
                        Float.floatToRawIntBits(f))));
    }

    @Override
    public void writeBoolean(Boolean b) throws IOException {
        out.writeBoolean(b);
    }

    @Override
    public void writeArray(Object[] array) throws Exception {
        writeLen(array.length);
        for (var item: array) {
            new KineticStreamWriter(this).write(item);
        }
    }

    @Override
    public void close() throws Exception {
        // nothing to release
    }
}
