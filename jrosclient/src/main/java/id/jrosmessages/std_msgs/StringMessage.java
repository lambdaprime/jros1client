package id.jrosmessages.std_msgs;

import java.util.Objects;

import id.jrosmessages.Message;
import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

@Message(
    type = "std_msgs/String",
    md5sum = "992ce8a1687cec8c8bd883ec73ca41d1")
public class StringMessage {
    
    @Streamed
    public String data = "";

    public StringMessage withData(String data) {
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
        StringMessage other = (StringMessage) obj;
        return Objects.equals(data, other.data);
    }
}
