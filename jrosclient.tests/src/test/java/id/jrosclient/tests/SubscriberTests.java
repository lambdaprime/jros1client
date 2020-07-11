package id.jrosclient.tests;

import static id.jrosclient.tests.TestConstants.CALLER_ID;
import static id.jrosclient.tests.TestConstants.PORT;
import static id.jrosclient.tests.TestConstants.TOPIC;

import java.net.MalformedURLException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import id.jrosclient.JRosClient;
import id.jrosclient.ros.NodeServer;
import id.jrosclient.ros.entities.Protocol;

public class SubscriberTests {
    
    private static JRosClient client;

    @BeforeAll
    public static void setup() throws MalformedURLException {
        client = new JRosClient("http://ubuntu:11311/");
    }

    @Test
    public void test_registerSubscriber() {
        try (var nodeServer = new NodeServer(PORT)) {
            var publishers = client.getMasterApi().registerSubscriber(CALLER_ID, TOPIC, "std_msgs/String",
                    nodeServer.getNodeApi());
            TestUtils.compareWithTemplate(publishers.toString(), "test_registerSubscriber1");
            var nodeApi = client.getNodeApi(publishers.value.get(0));
            var protocols = nodeApi.requestTopic(CALLER_ID, TOPIC, List.of(Protocol.TCPROS));
            TestUtils.compareWithTemplate(protocols.toString(), "test_registerSubscriber2");
        }
    }

}
