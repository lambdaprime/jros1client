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
import id.xfunction.logging.XLogger;
import id.xfunction.text.WildcardMatcher;

import static id.jrosclient.tests.integration.TestConstants.URL;
import static id.xfunction.XUtils.readResource;

public class JRosClientTests {

    private static JRosClient client;

    @BeforeEach
    public void setup() throws MalformedURLException {
        // restore logging
        XLogger.load("logging.properties");
        client = new JRosClient(URL);
    }

    @AfterEach
    public void clean() throws Exception {
        client.close();
    }

    @Test
    public void test_publish_single() throws Exception {
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
        test_publish_single();
        var actual = Files.readString(Paths.get("/tmp/jrosclient-test.log"));
        System.out.println(actual);
        Assertions.assertTrue(new WildcardMatcher(readResource("test_subscriber_truncate"))
                .matches(actual));
    }
}
