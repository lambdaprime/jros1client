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
