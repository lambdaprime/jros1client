package id.jrosclient.tests;

import static id.jrosclient.tests.TestConstants.*;
import static id.jrosclient.tests.TestUtils.compare;

import java.net.MalformedURLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import id.jrosclient.JRosClient;

public class MasterApiTests {
    
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
        var publishers = client.getMasterApi().registerSubscriber(CALLER_ID, TOPIC, "std_msgs/String");
        System.out.println(publishers);
        TestUtils.compareWithTemplate(publishers.toString(), "test_registerSubscriber1");
    }

    public static void main(String[] args) throws MalformedURLException, Exception {
        //System.out.println(client.getMasterApi().lookupService(CALLER_ID, "service"));
        var client = new JRosClient(URL, PORT);
        System.out.println("Master response: " + client.getMasterApi().registerPublisher(CALLER_ID, "visualization_marker",
                "visualization_msgs/Marker"));
        while(true);
    }
}
