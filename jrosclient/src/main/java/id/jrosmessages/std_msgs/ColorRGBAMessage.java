package id.jrosmessages.std_msgs;

import java.util.Objects;

import id.jrosmessages.Message;
import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

@Message(
    type = ColorRGBAMessage.NAME,
    md5sum = "a29a96539573343b1310c73607334b00")
public class ColorRGBAMessage {

    static final String NAME = "std_msgs/ColorRGBA";
    
    @Streamed
    public float r, g, b, a;

    public ColorRGBAMessage() {

    }

    public ColorRGBAMessage(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    @Override
    public String toString() {
        return XJson.asString("r", r,
                "g", g,
                "b", b,
                "a", a).toString();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(r, g, b, a);
    }
    
    @Override
    public boolean equals(Object obj) {
        ColorRGBAMessage other = (ColorRGBAMessage) obj;
        return Objects.equals(r, other.r)
                && Objects.equals(g, other.g)
                && Objects.equals(b, other.b)
                && Objects.equals(a, other.a);
    }
}
