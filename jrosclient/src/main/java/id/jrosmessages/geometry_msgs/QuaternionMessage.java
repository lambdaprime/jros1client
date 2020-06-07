package id.jrosmessages.geometry_msgs;

import java.util.Objects;

import id.jrosmessages.Message;
import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

@Message(
    type = QuaternionMessage.NAME,
    md5sum = "a779879fadf0160734f906b8c19c7004")
public class QuaternionMessage {

    static final String NAME = "geometry_msgs/Quaternion";

    public QuaternionMessage() {

    }

    public QuaternionMessage(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Streamed
    public double x, y, z, w;

    @Override
    public String toString() {
        return XJson.asString("x", x,
                "y", y,
                "z", z,
                "w", w).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }

    @Override
    public boolean equals(Object obj) {
        QuaternionMessage other = (QuaternionMessage) obj;
        return Objects.equals(x, other.x)
                && Objects.equals(y, other.y)
                && Objects.equals(z, other.z)
                && Objects.equals(w, other.w);
    }
}
