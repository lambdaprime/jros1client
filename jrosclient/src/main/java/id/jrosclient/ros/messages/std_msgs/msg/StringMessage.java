package id.jrosclient.ros.messages.std_msgs.msg;

import id.jrosclient.ros.messages.Message;
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
