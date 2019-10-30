package id.jrosclient.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.stream.Collectors;

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

    private void compare(String out, String file) {
        var str = "";
        try {
            str = new BufferedReader(new InputStreamReader(getClass().getResource(file).openStream())).lines()
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(str, out);
    }
    

    public static void main(String[] args) throws MalformedURLException, Exception {
        //System.out.println(client.getMasterApi().lookupService(CALLER_ID, "service"));
        var client = new JRosClient("http://ubuntu:11311/", 1234);
        System.out.println("Master response: " + client.getMasterApi().registerPublisher(CALLER_ID, "visualization_marker",
                "visualization_msgs/Marker"));
        while(true);
    }
}
