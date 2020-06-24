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
    
    @Streamed
    public double x, y, z;

    public Vector3Message withX(double x) {
        this.x = x;
        return this;
    }

    public Vector3Message withY(double y) {
        this.y = y;
        return this;
    }

    public Vector3Message withZ(double z) {
        this.z = z;
        return this;
    }

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
