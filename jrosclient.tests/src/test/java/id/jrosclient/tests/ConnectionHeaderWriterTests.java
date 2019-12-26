package id.jrosclient.tests;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import id.jrosclient.ros.transport.ConnectionHeaderWriter;
import id.jrosclient.tests.ConnectionHeaderSamples.ConnectionHeaderSample;
import id.jrosclient.tests.utils.OutputStreamCollector;

public class ConnectionHeaderWriterTests {

    private static Stream<ConnectionHeaderSample> headerSamples() {
        return Stream.of(ConnectionHeaderSamples.HEADER);
    }

    @ParameterizedTest
    @MethodSource("headerSamples")
    public void test_happy(ConnectionHeaderSample sample) throws Exception {
        OutputStreamCollector collector = new OutputStreamCollector();
        var dos = new DataOutputStream(collector);
        new ConnectionHeaderWriter(dos).write(sample.getHeader());
        List<String> expected = TestUtils.readFileAsStream(sample.getResource())
            .map(l -> Arrays.asList(l.split(" ")))
            .flatMap(List::stream)
            .collect(Collectors.toList());
        assertIterableEquals(expected, collector.output);
    }

}
