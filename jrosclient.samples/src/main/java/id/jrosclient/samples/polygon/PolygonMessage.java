package id.jrosclient.samples.polygon;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.jrosmessages.geometry_msgs.Point32Message;
import id.kineticstreamer.annotations.Streamed;

@MessageMetadata(
    type = PolygonMessage.NAME,
    md5sum = "cd60a26494a087f577976f0329fa120e")
public class PolygonMessage implements Message {

    static final String NAME = "geometry_msgs/Polygon";

    @Streamed
    public Point32Message[] points = new Point32Message[0];

    public PolygonMessage withPoints(Point32Message[] points) {
        this.points = points;
        return this;
    }

}
