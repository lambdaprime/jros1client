package id.jrosmessages.geometry_msgs;

import java.util.Objects;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

/**
 * Definition for geometry_msgs/Pose
 */
@MessageMetadata(
    type = PoseMessage.NAME,
    md5sum = "e45d45a5a1ce597b249e23fb30fc871f")
public class PoseMessage implements Message {

    static final String NAME = "geometry_msgs/Pose";
    
    @Streamed
    public PointMessage position = new PointMessage();
    
    @Streamed
    public QuaternionMessage orientation = new QuaternionMessage();

    public PoseMessage withPosition(PointMessage position) {
        this.position = position;
        return this;
    }

    public PoseMessage withQuaternion(QuaternionMessage orientation) {
        this.orientation = orientation;
        return this;
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
