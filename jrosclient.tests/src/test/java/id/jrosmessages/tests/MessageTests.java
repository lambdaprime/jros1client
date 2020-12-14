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
 * - lambdaprime <id.blackmesa@gmail.com>
 */
package id.jrosmessages.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import id.jrosmessages.std_msgs.ColorRGBAMessage;
import id.jrosmessages.std_msgs.HeaderMessage;
import id.jrosmessages.std_msgs.StringMessage;
import id.jrosmessages.visualization_msgs.MarkerMessage;
import id.jrosmessages.geometry_msgs.Point32Message;
import id.jrosmessages.geometry_msgs.PointMessage;
import id.jrosmessages.geometry_msgs.PolygonMessage;
import id.jrosmessages.geometry_msgs.PolygonStampedMessage;
import id.jrosmessages.geometry_msgs.PoseMessage;
import id.jrosmessages.geometry_msgs.QuaternionMessage;
import id.jrosmessages.geometry_msgs.Vector3Message;
import id.jrosmessages.impl.RosDataInput;
import id.jrosmessages.impl.RosDataOutput;
import id.jrosmessages.primitives.Duration;
import id.jrosmessages.primitives.Time;
import id.kineticstreamer.KineticStreamReader;
import id.kineticstreamer.KineticStreamWriter;
import id.xfunction.XUtils;
import id.xfunction.io.XInputStream;
import id.xfunction.io.XOutputStream;

public class MessageTests {
    
    static Stream<List> dataProvider() {
        return Stream.of(
                List.of(XUtils.readResource(MessageTests.class, "string-empty"),
                        new StringMessage()),
            List.of(XUtils.readResource(MessageTests.class, "string"),
                    new StringMessage().withData("hello there")),
            
            List.of(XUtils.readResource(MessageTests.class, "point-empty"),
                    new PointMessage()),
            List.of(XUtils.readResource(MessageTests.class, "point"),
                    new PointMessage().withX(1.0).withY(1.0).withZ(1.0)),
            
            List.of(XUtils.readResource(MessageTests.class, "point32"),
                    new Point32Message().withX(1.0F).withY(1.0F).withZ(1.0F)),

            List.of(XUtils.readResource(MessageTests.class, "polygonstamped"),
                    new PolygonStampedMessage()
                        .withHeader(new HeaderMessage()
                                .withSeq(123)
                                .withStamp(new Time(0, 1111))
                                .withFrameId("aaaa"))
                        .withPolygon(new PolygonMessage()
                                .withPoints(new Point32Message[]{
                                        new Point32Message(2F, 2F, 0F),
                                        new Point32Message(1F, 2F, 3F),
                                        new Point32Message(0F, 0F, 0F)}))),

            List.of(XUtils.readResource(MessageTests.class, "quaternion-empty"),
                    new QuaternionMessage()),
            List.of(XUtils.readResource(MessageTests.class, "quaternion"),
                    new QuaternionMessage().withX(1.0).withY(1.0).withZ(1.0).withW(3.0)),
            
            List.of(XUtils.readResource(MessageTests.class, "pose-empty"),
                    new PoseMessage()),
            List.of(XUtils.readResource(MessageTests.class, "pose"), new PoseMessage()
                    .withPosition(
                            new PointMessage().withX(1.0).withY(1.0).withZ(1.0))
                    .withQuaternion(
                            new QuaternionMessage().withX(1.0).withY(1.0).withZ(1.0).withW(3.0))),
            
            List.of(XUtils.readResource(MessageTests.class, "header-empty"),
                    new HeaderMessage()),
            List.of(XUtils.readResource(MessageTests.class, "header"),
                    new HeaderMessage()
                        .withSeq(123)
                        .withStamp(new Time(0, 1111))
                        .withFrameId("aaaa")),
            
            List.of(XUtils.readResource(MessageTests.class, "colorrgba-empty"),
                    new ColorRGBAMessage()),
            List.of(XUtils.readResource(MessageTests.class, "colorrgba"),
                    new ColorRGBAMessage().withR(.12F).withG(.13F).withB(.14F).withA(.15F)),
            
            List.of(XUtils.readResource(MessageTests.class, "vector3-empty"),
                    new Vector3Message()),
            List.of(XUtils.readResource(MessageTests.class, "vector3"),
                    new Vector3Message().withX(.12).withY(.13).withZ(.14)),

            List.of(XUtils.readResource(MessageTests.class, "marker-empty"),
                    new MarkerMessage()),
            List.of(XUtils.readResource(MessageTests.class, "marker"),
                    new MarkerMessage()
                        .withHeader(new HeaderMessage()
                                .withSeq(0)
                                .withFrameId("/map"))
                        .withNs(new StringMessage().withData("test"))
                        .withType(MarkerMessage.Type.CUBE)
                        .withAction(MarkerMessage.Action.ADD)
                        .withPose(new PoseMessage()
                                .withPosition(new PointMessage()
                                        .withX(1.0)
                                        .withY(1.0)
                                        .withZ(1.0))
                                .withQuaternion(new QuaternionMessage()
                                        .withX(1.0)
                                        .withY(1.0)
                                        .withZ(1.0)
                                        .withW(3.0)))
                        .withScale(new Vector3Message()
                                .withX(1.0)
                                .withY(0.1)
                                .withZ(0.1))
                        .withColor(new ColorRGBAMessage()
                                .withR(1.0F)
                                .withG(0.13F)
                                .withB(0.14F)
                                .withA(1.0F))
                        .withLifetime(new Duration())
                        .withFrameLocked(false))
        );
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void testRead(List testData) throws Exception {
        var collector = new XInputStream((String)testData.get(0));
        var dis = new RosDataInput(new DataInputStream(collector));
        var ks = new KineticStreamReader(dis);
        Object expected = testData.get(1);
        Object actual = ks.read(expected.getClass());
        System.out.println(actual);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void testWrite(List testData) throws Exception {
        var b = testData.get(1);
        var collector = new XOutputStream();
        var dos = new RosDataOutput(new DataOutputStream(collector));
        var ks = new KineticStreamWriter(dos);
        ks.write(b);
        assertEquals(testData.get(0), collector.asHexString());
    }
}
