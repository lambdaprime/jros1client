package id.jrosclient.tests;

import id.jrosclient.ros.transport.ConnectionHeader;
import id.xfunction.function.Unchecked;
import id.xfunction.XUtils;

public class ConnectionHeaderSamples {

    public static class ConnectionHeaderSample {
        private final String resource;
        private final ConnectionHeader header;
        
        ConnectionHeaderSample(String resource, ConnectionHeader header) {
            this.resource = resource;
            this.header = header;
        }
    
        String getResource() {
            return resource;
        }
    
        ConnectionHeader getHeader() {
            return header;
        }
    }
    
    private static final String MESSAGE_DATA = "string data";

    static final ConnectionHeaderSample HEADER = new ConnectionHeaderSample("connection_header", new ConnectionHeader()
            .withTopic("/topic")
            .withCallerId("jrosclient")
            .withType("std_msgs/String")
            .withMessageDefinition(MESSAGE_DATA)
            .withMd5Sum(Unchecked.runUnchecked(() -> XUtils.md5Sum(MESSAGE_DATA))));

}
