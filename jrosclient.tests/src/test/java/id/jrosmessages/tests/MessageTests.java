package id.jrosmessages.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import id.jrosmessages.RosDataInput;
import id.jrosmessages.RosDataOutput;
import id.jrosmessages.std_msgs.ColorRGBAMessage;
import id.jrosmessages.std_msgs.HeaderMessage;
import id.jrosmessages.std_msgs.StringMessage;
import id.jrosmessages.visualization_msgs.MarkerMessage;
import id.jrosmessages.geometry_msgs.PointMessage;
import id.jrosmessages.geometry_msgs.PoseMessage;
import id.jrosmessages.geometry_msgs.QuaternionMessage;
import id.jrosmessages.geometry_msgs.Vector3Message;
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
                    new HeaderMessage().withSeq(123).withStamp(new Time(0, 1111)).withFrameId("aaaa")),
            
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
