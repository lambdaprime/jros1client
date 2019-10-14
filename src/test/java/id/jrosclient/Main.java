package id.jrosclient;

import java.net.MalformedURLException;

import id.jrosclient.melodic.JRosClient;

public class Main {
    
    private static final String CALLER_ID = "jrosclient";

    public static void main(String[] args) throws MalformedURLException, Exception {
        var client = JRosClient.create("http://ubuntu:11311/");
        System.out.println(client.getMasterApi().getSystemState(CALLER_ID));
        System.out.println(client.getMasterApi().getUri(CALLER_ID));
        System.out.println(client.getMasterApi().lookupService(CALLER_ID, "service"));
        System.out.println(client.getMasterApi().registerPublisher(CALLER_ID, "visualization_marker",
                "visualization_msgs/Marker", "http://localhost:1111/"));
    }
}
