package id.jrosclient.tests.integration;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.jrosclient.JRosClient;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosclient.TopicSubscriber;
import id.jrosmessages.std_msgs.Int32Message;
import id.jrosmessages.std_msgs.StringMessage;

public class JRosClientTests {

    private static JRosClient client;

    @BeforeEach
    public void setup() throws MalformedURLException {
        client = new JRosClient("http://ubuntu:11311/");
    }

    @AfterEach
    public void clean() throws Exception {
        client.close();
    }

    @Test
    public void test_publish_single() throws Exception {
        var future = new CompletableFuture<String>();
        String topic = "/testTopic2";
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
}