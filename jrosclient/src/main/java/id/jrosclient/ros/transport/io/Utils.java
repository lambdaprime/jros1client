package id.jrosclient.ros.transport.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Utils {

    public int readLen(DataInput in) throws IOException {
        return Integer.reverseBytes(in.readInt());
    }
    
    public void writeLen(DataOutput out, int len) throws IOException {
        out.writeInt(Integer.reverseBytes(len));
    }
}
