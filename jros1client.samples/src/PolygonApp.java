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
import id.jros1client.JRos1Client;
import id.jros1client.JRos1ClientFactory;
import id.jros1messages.std_msgs.HeaderMessage;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.jrosmessages.geometry_msgs.Point32Message;
import id.jrosmessages.primitives.Time;
import id.kineticstreamer.annotations.Streamed;
import id.xfunction.cli.CommandLineInterface;

/**
 * Demonstrates how to define custom messages using PolygonStampedMessage
 * as an example.
 */
public class PolygonApp {

    public static void main(String[] args) throws Exception {
        var cli = new CommandLineInterface();
        String topic = "/PolygonExample";
        try (var client = new JRos1ClientFactory().createJRosClient("http://localhost:11311/")) {
            var publisher = new TopicSubmissionPublisher<>(PolygonStampedMessage.class, topic);
            client.publish(publisher);
            cli.print("Press any key to stop publishing...");
            while (!cli.wasKeyPressed()) {
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
                cli.print("Published");
                Thread.sleep(1000);
            }
        }
    }
}


/**
 * Example of custom message definition
 */
@MessageMetadata(
    type = PolygonMessage.NAME,
    md5sum = "cd60a26494a087f577976f0329fa120e")
class PolygonMessage implements Message {

    static final String NAME = "geometry_msgs/Polygon";

    @Streamed
    public Point32Message[] points = new Point32Message[0];

    public PolygonMessage withPoints(Point32Message[] points) {
        this.points = points;
        return this;
    }

}

/**
 * Example of custom message definition
 */
@MessageMetadata(
    type = PolygonStampedMessage.NAME,
    md5sum = "c6be8f7dc3bee7fe9e8d296070f53340")
class PolygonStampedMessage implements Message {

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