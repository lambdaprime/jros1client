package id.jrosclient.tests;

import java.net.MalformedURLException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import id.jrosclient.JRosClient;
import id.jrosclient.ros.entities.Protocol;

public class SubscriberTests {
    
    private static final String CALLER_ID = "jrosclient";
    private static JRosClient client;

    @BeforeAll
    public static void setup() throws MalformedURLException {
        client = new JRosClient("http://ubuntu:11311/", 1234);
    }

    @AfterAll
    public static void cleanup() throws Exception {
        client.close();
    }

    @Test
    public void test_registerSubscriber() {
        String topic = "topic";
        var publishers = client.getMasterApi().registerSubscriber(CALLER_ID, topic, "std_msgs/String");
        TestUtils.compareWithTemplate(publishers.toString(), "/test_registerSubscriber1");
        var nodeApi = client.getNodeApi(publishers.value.get(0));
        var protocols = nodeApi.requestTopic(CALLER_ID, topic, List.of(Protocol.TCPROS));
        TestUtils.compareWithTemplate(protocols.toString(), "/test_registerSubscriber2");
    }

    public static void main(String[] args) throws MalformedURLException, Exception {
        JRosClient client = new JRosClient("http://ubuntu:11311/", 1234);
        String topic = "topic";
        var publishers = client.getMasterApi().registerSubscriber(CALLER_ID, topic, "std_msgs/String");
        System.out.println(publishers);
        var nodeApi = client.getNodeApi(publishers.value.get(0));
        var protocols = nodeApi.requestTopic(CALLER_ID, topic, List.of(Protocol.TCPROS));
        System.out.println(protocols);
    }
}
