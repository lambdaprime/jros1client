package id.jrosclient.ros.responses;

import id.xfunction.XJson;

public class IntResponse extends Response {

    public final String key;
    public int value;

    public IntResponse(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return XJson.merge(super.toString(), XJson.asString(
                key, value));
    }
}
