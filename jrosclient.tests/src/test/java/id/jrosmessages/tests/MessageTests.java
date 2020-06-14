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
import id.jrosmessages.geometry_msgs.PointMessage;
import id.jrosmessages.geometry_msgs.PoseMessage;
import id.jrosmessages.geometry_msgs.QuaternionMessage;
import id.jrosmessages.primitives.Time;
import id.kineticstreamer.InputStreamByteList;
import id.kineticstreamer.KineticStreamReader;
import id.kineticstreamer.KineticStreamWriter;
import id.kineticstreamer.OutputStreamByteList;
import id.xfunction.XUtils;

public class MessageTests {
    
    static Stream<List> dataProvider() {
        return Stream.of(
            List.of(XUtils.readResource(MessageTests.class, "string"),
                    new StringMessage("hello there")),
            List.of(XUtils.readResource(MessageTests.class, "point"),
                    new PointMessage(1.0, 1.0, 1.0)),
            List.of(XUtils.readResource(MessageTests.class, "quaternion"),
                    new QuaternionMessage(1.0, 1.0, 1.0, 3.0)),
            List.of(XUtils.readResource(MessageTests.class, "pose"),
                    new PoseMessage(new PointMessage(1.0, 1.0, 1.0), new QuaternionMessage(1.0, 1.0, 1.0, 3.0))),
            List.of(XUtils.readResource(MessageTests.class, "header"),
                    new HeaderMessage(123, new Time(0, 1111), "aaaa")),
            List.of(XUtils.readResource(MessageTests.class, "colorrgba"),
                    new ColorRGBAMessage(.12F, .13F, .14F, .15F))
        );
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void testRead(List testData) throws Exception {
        var collector = new InputStreamByteList((String)testData.get(0));
        var dis = new RosDataInput(new DataInputStream(collector));
        var ks = new KineticStreamReader(dis);
        Object expected = testData.get(1);
        Object actual = expected.getClass().getConstructor().newInstance();
        ks.read(actual);
        System.out.println(actual);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void testWrite(List testData) {
        var b = testData.get(1);
        OutputStreamByteList collector = new OutputStreamByteList();
        var dos = new RosDataOutput(new DataOutputStream(collector));
        var ks = new KineticStreamWriter(dos);
        ks.write(b);
        assertEquals(testData.get(0), collector.asHexString());
    }
}
