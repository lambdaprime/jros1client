package id.jrosclient.ros.entities;

import java.util.List;

public class Protocol implements Entity {

    public static Protocol TCPROS = new Protocol("TCPROS");

//    public Protocol() {}

    public Protocol(String name) {
        protocolName = name;
    }

    public String protocolName;
    public List<String> protocolParams = List.of();

}