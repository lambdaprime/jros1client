package id.jrosmessages.std_msgs;

import java.util.Objects;

import id.jrosmessages.Message;
import id.jrosmessages.primitives.Time;
import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

@Message(
    type = "std_msgs/Header",
    md5sum = "2176decaecbce78abc3b96ef049fabed")
public class HeaderMessage {
    
    @Streamed
    public int seq;
    
    @Streamed
    public Time stamp;
    
    @Streamed
    public String frame_id;
    
    public HeaderMessage() {

    }
    
    public HeaderMessage(int seq, Time stamp, String frame_id) {
        this.seq = seq;
        this.stamp = stamp;
        this.frame_id = frame_id;
    }

    @Override
    public String toString() {
        return XJson.asString("seq", "" + seq,
                "stamp", stamp,
                "frame_id", frame_id).toString();
    }
    
    @Override
    public int hashCode() {
        return seq + Objects.hash(stamp, frame_id);
    }
    
    @Override
    public boolean equals(Object obj) {
        HeaderMessage other = (HeaderMessage) obj;
        return seq == other.seq
                && Objects.equals(stamp, other.stamp)
                && Objects.equals(frame_id, other.frame_id);
    }
}
