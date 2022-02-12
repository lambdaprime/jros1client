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
package id.jrosclient.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import id.jrosclient.ros.transport.ConnectionHeader;
import id.jrosclient.ros.transport.io.ConnectionHeaderWriter;
import id.jrosclient.tests.ConnectionHeaderSamples.ConnectionHeaderSample;
import id.xfunction.ResourceUtils;
import id.xfunction.io.XOutputStream;

public class ConnectionHeaderWriterTests {

    private static final ResourceUtils resourceUtils = new ResourceUtils();

    private static Stream<ConnectionHeaderSample> headerSamples() {
        return Stream.of(ConnectionHeaderSamples.HEADER);
    }

    @ParameterizedTest
    @MethodSource("headerSamples")
    public void test_happy(ConnectionHeaderSample sample) throws Exception {
        var collector = new XOutputStream();
        var dos = new DataOutputStream(collector);
        new ConnectionHeaderWriter(dos).write(sample.getHeader());
        var expected = resourceUtils.readResourceAsStream(sample.getResource())
                .map(l -> Arrays.asList(l.split(" ")))
                .flatMap(List::stream)
                .collect(Collectors.joining(", "));
        assertEquals(expected, collector.asHexString());
    }

    @Test
    public void test_write_empty_header() throws Exception {
        var collector = new XOutputStream();
        var dos = new DataOutputStream(collector);
        new ConnectionHeaderWriter(dos).write(new ConnectionHeader());
        assertEquals("", collector.asHexString());
    }
}
