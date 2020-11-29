package id.jrosmessages.std_msgs;

import java.util.Objects;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.jrosmessages.primitives.Time;
import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

/**
 * Definition for std_msgs/Header
 */
@MessageMetadata(
    type = "std_msgs/Header",
    md5sum = "2176decaecbce78abc3b96ef049fabed")
public class HeaderMessage implements Message {
    
    @Streamed
    public int seq;
    
    @Streamed
    public Time stamp = new Time();
    
    @Streamed
    public String frame_id = "";
    
    public HeaderMessage withSeq(int seq) {
        this.seq = seq;
        return this;
    }
    
    public HeaderMessage withStamp(Time stamp) {
        this.stamp = stamp;
        return this;
    }

    public HeaderMessage withFrameId(String frame_id) {
        this.frame_id = frame_id;
        return this;
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
