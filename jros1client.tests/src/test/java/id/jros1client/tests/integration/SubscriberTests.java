/*
 * Copyright 2020 jrosclient project
 * 
 * Website: https://github.com/lambdaprime/jros1client
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
package id.jros1client.tests.integration;

import static id.jros1client.tests.integration.TestConstants.CALLER_ID;
import static id.jros1client.tests.integration.TestConstants.TOPIC;
import static id.jros1client.tests.integration.TestConstants.URL;

import id.jros1client.JRos1Client;
import id.jros1client.JRos1ClientConfiguration;
import id.jros1client.JRos1ClientFactory;
import id.jros1client.ros.entities.Protocol;
import id.jros1client.tests.TestUtils;
import java.net.MalformedURLException;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** @author lambdaprime intid@protonmail.com */
public class SubscriberTests {

    private static final JRos1ClientFactory factory = new JRos1ClientFactory();
    private static JRos1Client client;

    @BeforeAll
    public static void setup() throws MalformedURLException {
        client = factory.createSpecializedJRos1Client(URL);
    }

    @Test
    public void test_registerSubscriber() {
        var configuration = new JRos1ClientConfiguration();
        var publishers =
                client.getMasterApi()
                        .registerSubscriber(
                                CALLER_ID, TOPIC, "std_msgs/String", configuration.getNodeApiUrl());
        TestUtils.compareWithTemplate(publishers.toString(), "test_registerSubscriber1");
        var nodeApi = client.getNodeApi(publishers.value.get(0));
        var protocols = nodeApi.requestTopic(CALLER_ID, TOPIC, List.of(Protocol.TCPROS));
        System.out.println(protocols);
        TestUtils.compareWithTemplate(protocols.toString(), "test_registerSubscriber2");
    }
}
