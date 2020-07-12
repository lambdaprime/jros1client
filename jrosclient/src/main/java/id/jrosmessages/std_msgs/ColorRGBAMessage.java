package id.jrosmessages.std_msgs;

import java.util.Objects;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

@MessageMetadata(
    type = ColorRGBAMessage.NAME,
    md5sum = "a29a96539573343b1310c73607334b00")
public class ColorRGBAMessage implements Message {

    static final String NAME = "std_msgs/ColorRGBA";
    
    @Streamed
    public float r, g, b, a;

    public ColorRGBAMessage withR(float r) {
        this.r = r;
        return this;
    }

    public ColorRGBAMessage withG(float g) {
        this.g = g;
        return this;
    }
    
    public ColorRGBAMessage withB(float b) {
        this.b = b;
        return this;
    }
    
    public ColorRGBAMessage withA(float a) {
        this.a = a;
        return this;
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
