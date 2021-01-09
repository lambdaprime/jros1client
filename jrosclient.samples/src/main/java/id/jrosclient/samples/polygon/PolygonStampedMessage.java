package id.jrosclient.samples.polygon;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.jrosmessages.std_msgs.HeaderMessage;
import id.kineticstreamer.annotations.Streamed;

@MessageMetadata(
    type = PolygonStampedMessage.NAME,
    md5sum = "c6be8f7dc3bee7fe9e8d296070f53340")
public class PolygonStampedMessage implements Message {

    static final String NAME = "geometry_msgs/PolygonStamped";

    @Streamed
    public HeaderMessage header = new HeaderMessage();
    
    @Streamed
    public PolygonMessage polygon = new PolygonMessage();

    public PolygonStampedMessage withPolygon(PolygonMessage polygon) {
        this.polygon = polygon;
        return this;
    }

    public PolygonStampedMessage withHeader(HeaderMessage header) {
        this.header = header;
        return this;
    }

}