package id.jrosclient.samples.polygon;

import id.jrosclient.JRosClient;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosmessages.geometry_msgs.Point32Message;
import id.jrosmessages.primitives.Time;
import id.jrosmessages.std_msgs.HeaderMessage;

public class PolygonApp {

    public static void main(String[] args) throws Exception {
        var client = new JRosClient("http://ubuntu:11311/");
        String topic = "/PolygonExample";
        var publisher = new TopicSubmissionPublisher<>(PolygonStampedMessage.class, topic);
        client.publish(publisher);
        while (true) {
            PolygonStampedMessage polygon = new PolygonStampedMessage()
                    .withHeader(new HeaderMessage()
                            .withFrameId("/map")
                            .withStamp(Time.now()))
                    .withPolygon(new PolygonMessage()
                            .withPoints(new Point32Message[]{
                                    new Point32Message(2F, 2F, 0F),
                                    new Point32Message(1F, 2F, 3F),
                                    new Point32Message(0F, 0F, 0F)}));
            publisher.submit(polygon);
            System.out.println("Published");
            Thread.sleep(1000);
        }
    }
}