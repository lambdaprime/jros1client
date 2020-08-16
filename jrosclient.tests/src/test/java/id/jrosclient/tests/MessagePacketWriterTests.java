package id.jrosclient.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import id.jrosclient.ros.transport.MessagePacket;
import id.jrosclient.ros.transport.io.MessagePacketWriter;
import id.jrosclient.tests.ConnectionHeaderSamples.ConnectionHeaderSample;
import id.xfunction.XByte;
import id.xfunction.XUtils;
import id.xfunction.io.XOutputStream;

public class MessagePacketWriterTests {

    private static Stream<List> headerSamples() {
        return Stream.of(buildTestSample(ConnectionHeaderSamples.HEADER));
    }

    @ParameterizedTest
    @MethodSource("headerSamples")
    public void test_happy(List sample) throws Exception {
        MessagePacket packet = (MessagePacket) sample.get(0);
        String expectedHexString = (String) sample.get(1);
        var collector = new XOutputStream();
        var dos = new DataOutputStream(collector);
        new MessagePacketWriter(dos).write(packet);
        assertEquals(expectedHexString, collector.asHexString());
    }

    private static List buildTestSample(ConnectionHeaderSample headerSample) {
        var hexString = XUtils.readResourceAsStream(headerSample.getResource())
                .map(l -> Arrays.asList(l.split(" ")))
                .flatMap(List::stream)
                .collect(Collectors.joining(", "));
        var body = "hello ros".getBytes();
        hexString += ", " + XByte.toHexPairs(Integer.reverseBytes(body.length))
            .replace(" ", ", ");
        hexString += ", " + XByte.toHexPairs(body).replace(" ", ", ");
        var packet = new MessagePacket(headerSample.getHeader(), body);
        return List.of(packet, hexString);
    }
}
