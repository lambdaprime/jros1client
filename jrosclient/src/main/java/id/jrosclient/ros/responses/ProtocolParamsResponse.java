package id.jrosclient.ros.responses;

import id.xfunction.XJson;

public class ProtocolParamsResponse extends Response {

    public String name;
    public String host;
    public int port;

    @Override
    public String toString() {
        return XJson.asString("name", name,
                "host", host,
                "port", port);
    }
}
