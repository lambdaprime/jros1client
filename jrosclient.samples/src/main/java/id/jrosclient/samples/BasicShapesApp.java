package id.jrosclient.samples;

import java.util.EnumSet;
import java.util.LinkedList;

import id.jrosclient.JRosClient;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosmessages.geometry_msgs.PointMessage;
import id.jrosmessages.geometry_msgs.PoseMessage;
import id.jrosmessages.geometry_msgs.QuaternionMessage;
import id.jrosmessages.geometry_msgs.Vector3Message;
import id.jrosmessages.primitives.Duration;
import id.jrosmessages.primitives.Time;
import id.jrosmessages.std_msgs.ColorRGBAMessage;
import id.jrosmessages.std_msgs.HeaderMessage;
import id.jrosmessages.std_msgs.StringMessage;
import id.jrosmessages.visualization_msgs.MarkerMessage;
import id.jrosmessages.visualization_msgs.MarkerMessage.Action;
import id.jrosmessages.visualization_msgs.MarkerMessage.Type;
import id.xfunction.lang.XRuntime;
import id.xfunction.lang.XThread;

/**
 * Example which demonstrates how to send basic shapes from Java
 * to RViz using jrosclient module. Shapes are sent every second.
 *
 * Based on http://wiki.ros.org/rviz/Tutorials/Markers%3A%20Basic%20Shapes
 */
public class BasicShapesApp {

    public static void main(String[] args) throws Exception {
        var client = new JRosClient("http://ubuntu:11311/");
        String topic = "/BasicShapesExampleXXX";
        var publisher = new TopicSubmissionPublisher<>(MarkerMessage.class, topic);
        client.publish(publisher);
        XRuntime.addShutdownHook(() -> {
            client.unpublish(topic);
            client.close();
        });
        var deque = new LinkedList<Type>(EnumSet.of(
                Type.CUBE,
                Type.SPHERE,
                Type.CYLINDER));
        while (true) {
            Type shape = deque.removeFirst();
            MarkerMessage marker = new MarkerMessage()
                    .withHeader(new HeaderMessage()
                            .withFrameId("/map")
                            .withStamp(Time.now()))
                    .withNs(new StringMessage().withData("basic_shapes"))
                    .withId(0)
                    .withType(shape)
                    .withAction(Action.ADD)
                    .withPose(new PoseMessage()
                            .withPosition(new PointMessage())
                            .withQuaternion(new QuaternionMessage()
                                    .withW(1.0)))
                    .withScale(new Vector3Message(1., 1., 1.))
                    .withColor(ColorRGBAMessage.RED)
                    .withLifetime(new Duration());
            publisher.submit(marker);
            System.out.println("Published");
            deque.add(shape);
            XThread.sleep(1000);
        }
    }
}
