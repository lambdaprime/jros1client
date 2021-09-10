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
package id.jrosclient.tests.integration;

import static id.jrosclient.tests.integration.TestConstants.URL;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.EOFException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.jrosclient.JRosClient;
import id.jrosclient.JRosClientConfiguration;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosclient.TopicSubscriber;
import id.jrosmessages.std_msgs.Int32Message;
import id.jrosmessages.std_msgs.StringMessage;
import id.xfunction.ResourceUtils;
import id.xfunction.lang.XThread;
import id.xfunction.logging.XLogger;
import id.xfunction.text.WildcardMatcher;

public class JRosClientTests {

    private static final ResourceUtils resourceUtils = new ResourceUtils();
    private static JRosClient client;

    @BeforeEach
    public void setup() throws MalformedURLException {
        // restore logging
        XLogger.load("logging-debug.properties");
        client = new JRosClient(URL);
    }

    @AfterEach
    public void clean() throws Exception {
        client.close();
    }

    @Test
    public void test_publish() throws Exception {
        var future = new CompletableFuture<String>();
        String topic = "testTopic2";
        var publisher = new TopicSubmissionPublisher<>(StringMessage.class, topic);
        String data = "hello";
        client.publish(publisher);
        client.subscribe(new TopicSubscriber<>(StringMessage.class, topic) {
            @Override
            public void onNext(StringMessage item) {
                System.out.println(item);
                getSubscription().cancel();
                future.complete(item.data);
            }
        });
        while (!future.isDone()) {
            publisher.submit(new StringMessage().withData(data));
        }
        client.unpublish(topic);
        Assertions.assertEquals(data, future.get());
    }
    
    @Test
    public void test_publish_single_message() throws Exception {
        var future = new CompletableFuture<String>();
        String topic = "testTopic2";
        var publisher = new TopicSubmissionPublisher<>(StringMessage.class, topic);
        String data = "hello";
        client.publish(publisher);
        client.subscribe(new TopicSubscriber<>(StringMessage.class, topic) {
            @Override
            public void onNext(StringMessage item) {
                System.out.println(item);
                getSubscription().cancel();
                future.complete(item.data);
            }
        });
        // wait so that subscriber get time to register with ROS
        XThread.sleep(1000);
        publisher.submit(new StringMessage().withData(data));
        future.get();
        client.unpublish(topic);
        Assertions.assertEquals(data, future.get());
    }

    /**
     * Test that publisher delivers messages which are in its queue before
     * it is being closed.
     */
    @Test
    public void test_publisher_on_close() throws Exception {
        var future = new CompletableFuture<Void>();
        String topic = "testTopic2";
        String data = "hello";
        int[] c = new int[1];
        int totalNumOfMessages = 10;
        try (var publisherClient = new JRosClient(URL);
                var publisher = new TopicSubmissionPublisher<>(StringMessage.class, topic);) {
            publisherClient.publish(publisher);

            client.subscribe(new TopicSubscriber<>(StringMessage.class, topic) {
                @Override
                public void onNext(StringMessage item) {
                    System.out.println(item);
                    Assertions.assertEquals(data, item.data);
                    c[0]++;
                    if (c[0] == totalNumOfMessages) {
                        getSubscription().cancel();
                        future.complete(null);
                    } else {
                        // delay requesting next message to make
                        // them accumulate on publisher
                        XThread.sleep(100);
                        request(1);
                    }
                }
            });

            // wait so that subscriber get time to register with ROS
            XThread.sleep(1000);
            for (int i = 0 ; i < totalNumOfMessages; i++) {
                publisher.submit(new StringMessage().withData(data));
            }
        }
        future.get();
        Assertions.assertEquals(totalNumOfMessages, c[0]);
    }
    
    @Test
    public void test_unpublish() throws Exception {
        var topic = "/testTopic3";
        Assertions.assertFalse(client.hasPublisher(topic));
        var publisher = new TopicSubmissionPublisher<>(StringMessage.class, topic);
        client.publish(publisher);
        Assertions.assertTrue(client.hasPublisher(topic));
        client.unpublish(topic);
        Assertions.assertFalse(client.hasPublisher(topic));
    }

    @Test
    public void test_publish_order() throws Exception {
        var future = new CompletableFuture<List<Integer>>();
        String topic = "/testTopic2";
        var publisher = new TopicSubmissionPublisher<>(Int32Message.class, topic);
        client.publish(publisher);
        client.subscribe(new TopicSubscriber<>(Int32Message.class, topic) {
            List<Integer> data = new ArrayList<>();
            @Override
            public void onNext(Int32Message item) {
                System.out.println(item);
                data.add(item.data);
                if (data.size() == 50) {
                    getSubscription().cancel();
                    future.complete(data);
                } else {
                    request(1);
                }
            }
        });
        int c = 0;
        while (!future.isDone()) {
            var msg = new Int32Message().withData(c++);
            System.out.println("          " + msg);
            publisher.submit(msg);
        }
        client.unpublish(topic);
        var received = future.get();
        var start = received.get(0);
        for (int i = 0; i < received.size(); i++) {
            Assertions.assertEquals(start + i, received.get(i));
        }
    }
    
    /**
     * Test that when publisher unexpectedly closes connection, subscriber notified
     * about this through onError
     */
    @Test
    public void test_publisher_crash() throws Exception {
        var future = new CompletableFuture<List<Integer>>();
        String topic = "/testTopic2";
        var publisher = new TopicSubmissionPublisher<>(Int32Message.class, topic);
        client.publish(publisher);
        client.subscribe(new TopicSubscriber<>(Int32Message.class, topic) {
            List<Integer> data = new ArrayList<>();
            @Override
            public void onNext(Int32Message item) {
                System.out.println(item);
            }
            public void onError(Throwable throwable) {
                Assertions.assertTrue(throwable instanceof EOFException);
                future.complete(data);
            }
        });
        var msg = new Int32Message().withData(1);
        publisher.submit(msg);
        publisher.closeExceptionally(new Exception());
        future.join();
        client.unpublish(topic);
    }
    
    /**
     * Test that message truncation is working
     */
    @Test
    public void test_log_truncation() throws Exception {
        XLogger.load("logging-test.properties");
        var config = new JRosClientConfiguration();
        config.setMaxMessageLoggingLength(6);
        client = new JRosClient(URL, config);
        test_publish();
        var actual = Files.readString(Paths.get("/tmp/jrosclient-test.log"));
        System.out.println(actual);
        Assertions.assertTrue(new WildcardMatcher(resourceUtils.readResource("test_subscriber_truncate"))
                .matches(actual));
    }

    @Test
    public void test_cannot_connect() throws Exception {
        var topic = "/testTopic3";
        var objectsFactory = new TestObjectsFactory();
        Exception exception = null;
        try (var myclient = new JRosClient("http://localhost:12/", objectsFactory.createConfig(),
                objectsFactory);
                var publisher = new TopicSubmissionPublisher<>(StringMessage.class, topic);) {
            exception = assertThrows(Exception.class, () -> myclient.publish(publisher));
        } catch (Exception e) {
            System.out.println(e);
        }
        Assertions.assertEquals("Failed to read server's response: Connection refused (Connection refused)", exception.getCause().getMessage());
        Assertions.assertEquals(true, objectsFactory.nodeServer.isClosed());
        Assertions.assertEquals(true, objectsFactory.tcpRosServer.isClosed());
    }
}
