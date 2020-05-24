package id.jrosmessages.std_msgs;

import id.jrosmessages.Message;
import id.kineticstreamer.annotations.Streamed;

@Message(type = "std_msgs/String")
public class StringMessage {

    @Streamed
    public String data;

    @Override
    public String toString() {
        return "data: " + data;
    }
}
