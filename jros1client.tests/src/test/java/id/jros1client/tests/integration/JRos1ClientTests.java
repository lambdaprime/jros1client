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

import static id.jros1client.tests.integration.TestConstants.URL;
import static org.junit.jupiter.api.Assertions.assertThrows;

import id.jros1client.JRos1Client;
import id.jros1client.JRos1ClientConfiguration;
import id.jros1client.JRos1ClientFactory;
import id.jrosclient.JRosClient;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosclient.TopicSubscriber;
import id.jrosmessages.std_msgs.Int32Message;
import id.jrosmessages.std_msgs.StringMessage;
import id.xfunction.ResourceUtils;
import id.xfunction.concurrent.flow.FixedCollectorSubscriber;
import id.xfunction.lang.XThread;
import id.xfunction.logging.XLogger;
import id.xfunction.text.WildcardMatcher;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JRos1ClientTests {

    private static final ResourceUtils resourceUtils = new ResourceUtils();
    private static final JRos1ClientFactory factory = new JRos1ClientFactory();
    private static JRosClient client;

    @BeforeAll
    public static void setupAll() {
        XLogger.load("logging-test.properties");
    }

    @BeforeEach
    public void setup() throws MalformedURLException {
        client = factory.createClient(URL);
    }

    @AfterEach
    public void clean() throws Exception {
        client.close();
    }

    /**
     * Test that subscriber works with foreign ROS publisher (not with publisher of {@link
     * JRos1Client})
     */
    @Test
    public void test_subscriber() throws Exception {
        String topic = "/testTopic";
        var collector = new FixedCollectorSubscriber<>(new ArrayList<StringMessage>(), 1);
        client.subscribe(topic, StringMessage.class, collector);
        var actual = collector.getFuture().get();
        var expected = new StringMessage("hello there");
        Assertions.assertEquals(actual.get(0), expected);
        Assertions.assertEquals(true, actual.stream().allMatch(Predicate.isEqual(expected)));
    }

    @Test
    public void test_unpublish() throws Exception {
        var topic = "/testTopic3";
        Assertions.assertFalse(client.hasPublisher(topic));
        var publisher = new TopicSubmissionPublisher<>(StringMessage.class, topic);
        client.publish(publisher);
        Assertions.assertTrue(client.hasPublisher(topic));
        client.unpublish(topic, StringMessage.class);
        Assertions.assertFalse(client.hasPublisher(topic));
    }

    /**
     * Test that when publisher unexpectedly closes connection, subscriber notified about this
     * through onError
     */
    @Test
    public void test_publisher_crash() throws Exception {
        var future = new CompletableFuture<Void>();
        String topic = "/testTopic1";
        var publisher = new TopicSubmissionPublisher<>(Int32Message.class, topic);
        client.publish(publisher);
        client.subscribe(
                new TopicSubscriber<>(Int32Message.class, topic) {
                    @Override
                    public void onNext(Int32Message item) {
                        System.out.println(item);
                    }

                    public void onError(Throwable throwable) {
                        System.out.println("onError occured: " + throwable);
                        future.complete(null);
                    }
                });
        var msg = new Int32Message().withData(1);
        publisher.submit(msg);
        publisher.closeExceptionally(new Exception());
        future.get();
        System.out.println("Awake");
        client.unpublish(topic, Int32Message.class);
    }

    /** Test that message truncation is working */
    @Test
    public void test_log_truncation() throws Exception {
        var config = new JRos1ClientConfiguration();
        config.setMaxMessageLoggingLength(6);
        client = factory.createClient(URL, config);
        String topic = "testTopic1";
        var publisher = new TopicSubmissionPublisher<>(StringMessage.class, topic);
        String data = "hello";
        client.publish(publisher);
        var collector = new FixedCollectorSubscriber<>(new ArrayList<StringMessage>(), 1);
        client.subscribe(topic, StringMessage.class, collector);
        while (!collector.getFuture().isDone()) {
            XThread.sleep(300);
            publisher.submit(new StringMessage().withData(data));
        }
        client.unpublish(topic, StringMessage.class);
        Assertions.assertEquals(data, collector.getFuture().get().get(0).data);
        var actual = Files.readString(Paths.get(TestConstants.LOG_FILE));
        System.out.println(actual);
        Assertions.assertTrue(
                new WildcardMatcher(resourceUtils.readResource("test_subscriber_truncate"))
                        .matches(actual));
    }

    @Test
    public void test_cannot_connect() throws Exception {
        var topic = "/testTopic1";
        var objectsFactory = new TestObjectsFactory();
        Exception exception = null;
        try (var myclient =
                        factory.createClient(
                                "http://localhost:12/",
                                objectsFactory.createConfig(),
                                objectsFactory);
                var publisher = new TopicSubmissionPublisher<>(StringMessage.class, topic)) {
            exception = assertThrows(Exception.class, () -> myclient.publish(publisher));
        } catch (Exception e) {
            System.out.println(e);
        }
        Assertions.assertEquals(
                true,
                exception
                        .getCause()
                        .getMessage()
                        .startsWith("Failed to read server's response: Connection refused"));
        Assertions.assertEquals(true, objectsFactory.nodeServer.isClosed());
        Assertions.assertEquals(true, objectsFactory.tcpRosServer.isClosed());
    }
}
