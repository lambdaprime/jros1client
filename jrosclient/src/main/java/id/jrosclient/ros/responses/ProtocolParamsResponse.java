package id.jrosclient.ros.responses;

import id.jrosclient.ros.entities.Protocol;
import id.xfunction.XJson;

public class ProtocolParamsResponse extends Response {

    public String name;
    public String host;
    public int port;

    public ProtocolParamsResponse withProtocol(Protocol protocol) {
        this.name = protocol.protocolName;
        return this;
    }
    
    public ProtocolParamsResponse withHost(String host) {
        this.host = host;
        return this;
    }
    
    public ProtocolParamsResponse withPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public String toString() {
        return XJson.merge(super.toString(), XJson.asString("name", name,
                "host", host,
                "port", port));
    }
}
