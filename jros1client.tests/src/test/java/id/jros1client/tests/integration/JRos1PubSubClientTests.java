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

import id.jros1client.JRos1Client;
import id.jros1client.JRos1ClientFactory;
import id.jrosclient.JRosClient;
import id.jrosclient.tests.integration.JRosPubSubClientTests;
import id.pubsubtests.PubSubClientTestCase;
import id.xfunction.logging.XLogger;
import java.time.Duration;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;

/**
 * Test that Publisher and Subscriber of {@link JRos1Client} can interact with each other.
 *
 * @author lambdaprime intid@protonmail.com
 */
public class JRos1PubSubClientTests extends JRosPubSubClientTests {

    private static final JRos1ClientFactory factory = new JRos1ClientFactory();

    static {
        Supplier<JRosClient> supplier = factory::createClient;
        init(
                supplier,
                new TestCase(
                        "test_jros1client", factory::createClient, Duration.ofSeconds(2), 100));
    }

    @BeforeAll
    public static void setupAll() {
        XLogger.load("logging-test.properties");
    }

    @Override
    public void test_multiple_subscribers_same_topic(PubSubClientTestCase testCase)
            throws Exception {}

    /**
     * Disabled for ROS1.
     *
     * <p>In ROS1, when we call {@link JRosClient#publish(id.jrosclient.TopicPublisher) we do not
     * subscribe to the {@link TopicPublisher} immediately but wait until discovering new subscriber.
     * It means that publishing to {@link TopicPublisher} will never block as there are no subscriber queues
     * to fill up.
     */
    @Disabled
    @Override
    public void test_publish_when_no_subscribers(PubSubClientTestCase testCase) {}
}
