package id.jrosclient.ros.entities;

import java.util.List;

import id.xfunction.XJson;

public class Protocol implements Entity {

    public static Protocol TCPROS = new Protocol("TCPROS");

    public String protocolName;
    public List<String> protocolParams = List.of();

    public Protocol(String name) {
        protocolName = name;
    }

    @Override
    public String toString() {
        return XJson.asString("name", protocolName);
    }
}
