package id.jrosclient.ros.messages.std_msgs.msg;

import id.jrosclient.ros.messages.Message;
import id.kineticstreamer.annotations.Streamed;

public class StringMessage implements Message {

    @Streamed
    public String data;

    @Override
    public String getType() {
        return "std_msgs/String";
    }
}
