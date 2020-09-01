package id.jrosclient.tests.integration;

import java.net.MalformedURLException;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import id.jrosclient.JRosClient;
import id.jrosclient.TopicSubmissionPublisher;
import id.jrosclient.TopicSubscriber;
import id.jrosmessages.std_msgs.StringMessage;

public class JRosClientTests {

    private static JRosClient client;

    @BeforeAll
    public static void setup() throws MalformedURLException {
        client = new JRosClient("http://ubuntu:11311/");
    }

    @Test
    public void test_publish() throws Exception {
        var future = new CompletableFuture<String>();
        var publisher = new TopicSubmissionPublisher<>(StringMessage.class, "/testTopic2");
        String data = "hello";
        client.publish(publisher);
        client.subscribe(new TopicSubscriber<>(StringMessage.class, "/testTopic2") {
            @Override
            public void onNext(StringMessage item) {
                System.out.println(item);
                future.complete(item.data);
            }
        });
        while (!future.isDone()) {
            publisher.submit(new StringMessage().withData(data));
        }
        Assertions.assertEquals(data, future.get());
    }
}
