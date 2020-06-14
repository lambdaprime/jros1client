package id.jrosmessages.geometry_msgs;

import java.util.Objects;

import id.jrosmessages.Message;
import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

@Message(
    type = PoseMessage.NAME,
    md5sum = "e45d45a5a1ce597b249e23fb30fc871f")
public class PoseMessage {

    static final String NAME = "geometry_msgs/Pose";
    
    @Streamed
    public PointMessage position;
    
    @Streamed
    public QuaternionMessage orientation;

    public PoseMessage() {
        
    }

    public PoseMessage(PointMessage position, QuaternionMessage orientation) {
        this.position = position;
        this.orientation = orientation;
    }

    @Override
    public String toString() {
        return XJson.asString("position", position,
                "orientation", orientation).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, orientation);
    }

    @Override
    public boolean equals(Object obj) {
        PoseMessage other = (PoseMessage) obj;
        return Objects.equals(position, other.position)
                && Objects.equals(orientation, other.orientation);
    }
}