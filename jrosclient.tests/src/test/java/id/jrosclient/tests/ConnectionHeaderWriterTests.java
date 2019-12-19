package id.jrosclient.tests;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import id.jrosclient.ros.transport.ConnectionHeader;
import id.jrosclient.ros.transport.ConnectionHeaderWriter;
import id.jrosclient.tests.utils.OutputStreamCollector;
import id.xfunction.XUtils;

public class ConnectionHeaderWriterTests {

    @Test
    public void test_happy() throws Exception {
        String topic = "topic";
        String type = "std_msgs/String";
        String messageDefinition = "string data";
        var ch = new ConnectionHeader()
                .withTopic("/" + topic)
                .withCallerId("jrosclient")
                .withType(type)
                .withMessageDefinition(messageDefinition )
                .withMd5Sum(XUtils.md5Sum(messageDefinition));
        OutputStreamCollector collector = new OutputStreamCollector();
        var dos = new DataOutputStream(collector);
        new ConnectionHeaderWriter(dos).write(ch);
        List<String> expected = TestUtils.readFileAsStream("/connection_header")
            .map(l -> Arrays.asList(l.split(" ")))
            .flatMap(List::stream)
            .collect(Collectors.toList());
        assertIterableEquals(expected, collector.output);
    }

}
