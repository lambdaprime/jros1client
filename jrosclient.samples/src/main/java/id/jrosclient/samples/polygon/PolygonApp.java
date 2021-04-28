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
/*
 * Authors:
 * - lambdaprime <intid@protonmail.com>
 */
package id.jrosclient.samples.polygon;

import id.jrosclient.JRosClient;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosmessages.geometry_msgs.Point32Message;
import id.jrosmessages.primitives.Time;
import id.jrosmessages.std_msgs.HeaderMessage;
import id.xfunction.CommandLineInterface;

/**
 * Demonstrates how to define custom messages using PolygonStampedMessage
 * as an example.
 */
public class PolygonApp {

    public static void main(String[] args) throws Exception {
        var cli = new CommandLineInterface();
        String topic = "/PolygonExample";
        try (var client = new JRosClient("http://localhost:11311/")) {
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