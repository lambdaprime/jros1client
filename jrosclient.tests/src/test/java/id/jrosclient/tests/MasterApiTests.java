package id.jrosclient.tests;

import static id.jrosclient.tests.TestUtils.compare;

import java.net.MalformedURLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import id.jrosclient.JRosClient;

public class MasterApiTests {
    
    private static final String CALLER_ID = "jrosclient";
    private static JRosClient client;

    @BeforeAll
    public static void setup() throws MalformedURLException {
        client = new JRosClient("http://ubuntu:11311/", 1234);
    }

    @Test
    public void test_getSystemState() {
        var out = client.getMasterApi().getSystemState(CALLER_ID);
        compare(out.toString(), "/test_getSystemState");
    }

    @Test
    public void test_getUri() {
        var out = client.getMasterApi().getUri(CALLER_ID);
        compare(out.toString(), "/test_getUri");
    }

    @Test
    public void test_registerSubscriber() {
        String topic = "topic";
        var publishers = client.getMasterApi().registerSubscriber(CALLER_ID, topic, "std_msgs/String");
        TestUtils.compareWithTemplate(publishers.toString(), "/test_registerSubscriber");
    }

    public static void main(String[] args) throws MalformedURLException, Exception {
        //System.out.println(client.getMasterApi().lookupService(CALLER_ID, "service"));
        var client = new JRosClient("http://ubuntu:11311/", 1234);
        System.out.println("Master response: " + client.getMasterApi().registerPublisher(CALLER_ID, "visualization_marker",
                "visualization_msgs/Marker"));
        while(true);
    }
}
