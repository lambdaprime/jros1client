package id.jrosclient.ros.responses;

public class ProtocolParamsResponse extends Response {

    public String name;
    public String host;
    public int port;

    @Override
    public String toString() {
        return "{" +
                "\"name\": \"" + name + "\", " +
                "\"host\": \"" + host + "\", " +
                "\"port\": " + port + "}";
    }
}
