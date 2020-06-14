package id.jrosmessages.geometry_msgs;

import java.util.Objects;

import id.jrosmessages.Message;
import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

@Message(
    type = Vector3Message.NAME,
    md5sum = "4a842b65f413084dc2b10fb484ea7f17")
public class Vector3Message {

    static final String NAME = "geometry_msgs/Vector3";

    public Vector3Message() {

    }

    public Vector3Message(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Streamed
    public double x, y, z;

    @Override
    public String toString() {
        return XJson.asString("x", x,
                "y", y,
                "z", z).toString();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
    
    @Override
    public boolean equals(Object obj) {
        Vector3Message other = (Vector3Message) obj;
        return Objects.equals(x, other.x)
                && Objects.equals(y, other.y)
                && Objects.equals(z, other.z);
    }
}
