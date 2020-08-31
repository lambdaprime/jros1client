package id.jrosclient.tests.integration;

import static id.jrosclient.tests.integration.TestConstants.CALLER_ID;
import static id.jrosclient.tests.integration.TestConstants.TOPIC;

import java.net.MalformedURLException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import id.jrosclient.JRosClient;
import id.jrosclient.ros.NodeServer;
import id.jrosclient.ros.entities.Protocol;
import id.jrosclient.tests.TestUtils;

public class SubscriberTests {
    
    private static JRosClient client;

    @BeforeAll
    public static void setup() throws MalformedURLException {
        client = new JRosClient("http://ubuntu:11311/");
    }

    @Test
    public void test_registerSubscriber() {
        try (var nodeServer = new NodeServer()) {
            var publishers = client.getMasterApi().registerSubscriber(CALLER_ID, TOPIC, "std_msgs/String",
                    nodeServer.getNodeApi());
            TestUtils.compareWithTemplate(publishers.toString(), "test_registerSubscriber1");
            var nodeApi = client.getNodeApi(publishers.value.get(0));
            var protocols = nodeApi.requestTopic(CALLER_ID, TOPIC, List.of(Protocol.TCPROS));
            System.out.println(protocols);
            TestUtils.compareWithTemplate(protocols.toString(), "test_registerSubscriber2");
        }
    }

}
