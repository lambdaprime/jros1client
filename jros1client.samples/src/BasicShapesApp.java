/*
 * Copyright 2020 jrosclient project
 * 
 * Website: https://github.com/lambdaprime/jrosclient
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import id.jros1client.JRos1ClientFactory;
import id.jros1messages.std_msgs.HeaderMessage;
import id.jros1messages.visualization_msgs.MarkerMessage;
import id.jros1messages.visualization_msgs.MarkerMessage.Action;
import id.jros1messages.visualization_msgs.MarkerMessage.Type;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosmessages.geometry_msgs.PointMessage;
import id.jrosmessages.geometry_msgs.PoseMessage;
import id.jrosmessages.geometry_msgs.QuaternionMessage;
import id.jrosmessages.geometry_msgs.Vector3Message;
import id.jrosmessages.primitives.Duration;
import id.jrosmessages.primitives.Time;
import id.jrosmessages.std_msgs.ColorRGBAMessage;
import id.jrosmessages.std_msgs.StringMessage;
import id.xfunction.cli.CommandLineInterface;
import id.xfunction.lang.XThread;
import java.util.EnumSet;
import java.util.LinkedList;

/**
 * Example which demonstrates how to send basic shapes from Java
 * to RViz using jrosclient module. Shapes are sent every second.
 *
 * Based on http://wiki.ros.org/rviz/Tutorials/Markers%3A%20Basic%20Shapes
 */
public class BasicShapesApp {

    public static void main(String[] args) throws Exception {
        var cli = new CommandLineInterface();

        // define topic name where to publish
        String topic = "BasicShapesExampleXXX";

        // creating client and making it to connect to given master node URL
        try (var client = new JRos1ClientFactory().createClient("http://localhost:11311/")) {

            // creating publisher for a topic
            var publisher = new TopicSubmissionPublisher<>(MarkerMessage.class, topic);

            // registering with ROS master node
            client.publish(publisher);

            // set of shapes to iterate on
            var deque = new LinkedList<Type>(EnumSet.of(
                    Type.CUBE,
                    Type.SPHERE,
                    Type.CYLINDER));
            
            cli.print("Press any key to stop publishing...");
            
            while (!cli.wasKeyPressed()) {
                Type shape = deque.removeFirst();

                // creating a new message and populating it
                MarkerMessage marker = new MarkerMessage()
                        .withHeader(new HeaderMessage()
                                .withFrameId("map")
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

                // publishing message
                publisher.submit(marker);
                
                deque.add(shape);
                cli.print("Published");
                XThread.sleep(1000);
            }
        }
    }
}
