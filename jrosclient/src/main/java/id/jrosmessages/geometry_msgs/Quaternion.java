package id.jrosmessages.geometry_msgs;

import java.util.Map;
import java.util.Objects;

import id.jrosmessages.Message;
import id.kineticstreamer.annotations.Streamed;

@Message(
    type = Quaternion.NAME,
    md5sum = "a779879fadf0160734f906b8c19c7004")
public class Quaternion {

    static final String NAME = "geometry_msgs/Quaternion";

    public Quaternion() {

    }

    public Quaternion(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    @Streamed
    public double x, y, z, w;

    @Override
    public String toString() {
        return Map.of("x", x,
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
        Quaternion other = (Quaternion) obj;
        return Objects.equals(x, other.x)
                && Objects.equals(y, other.y)
                && Objects.equals(z, other.z)
                && Objects.equals(w, other.w);
    }
}
