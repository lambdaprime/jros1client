package id.jrosclient.tests;

import static id.jrosclient.tests.TestConstants.CALLER_ID;
import static id.jrosclient.tests.TestConstants.PORT;
import static id.jrosclient.tests.TestConstants.TOPIC;
import static id.jrosclient.tests.TestUtils.compare;

import java.net.MalformedURLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import id.jrosclient.JRosClient;
import id.jrosclient.ros.NodeServer;

public class MasterApiTests {
    
    private static JRosClient client;

    @BeforeAll
    public static void setup() throws MalformedURLException {
        client = new JRosClient("http://ubuntu:11311/");
    }

    @Test
    public void test_getSystemState() {
        var out = client.getMasterApi().getSystemState(CALLER_ID);
        System.out.println(out);
        TestUtils.compareWithTemplate(out.toString(), "test_getSystemState");
    }

    @Test
    public void test_getUri() {
        var out = client.getMasterApi().getUri(CALLER_ID);
        compare(out.toString(), "test_getUri");
    }

    @Test
    public void test_registerSubscriber() {
        try (var nodeServer = new NodeServer(PORT)) {
            var publishers = client.getMasterApi().registerSubscriber(CALLER_ID, TOPIC, "std_msgs/String",
                    nodeServer.getNodeApi());
            System.out.println(publishers);
            TestUtils.compareWithTemplate(publishers.toString(), "test_registerSubscriber1");
        }
    }

}
