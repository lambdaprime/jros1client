package id.jrosmessages.std_msgs;

import java.util.Objects;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

/**
 * Definition for std_msgs/Int32
 */
@MessageMetadata(
    type = "std_msgs/Int32",
    md5sum = "da5909fbe378aeaf85e547e830cc1bb7")
public class Int32Message implements Message {
    
    @Streamed
    public int data;

    public Int32Message withData(int data) {
        this.data = data;
        return this;
    }
    
    @Override
    public String toString() {
        return XJson.asString("data", data).toString();
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(data);
    }
    
    @Override
    public boolean equals(Object obj) {
        Int32Message other = (Int32Message) obj;
        return Objects.equals(data, other.data);
    }
}
