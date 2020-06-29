package id.jrosclient.ros.responses;

import id.xfunction.XJson;

public class StringResponse extends Response {

    public final String key;
    public String value = "";

    public StringResponse(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return XJson.merge(super.toString(), XJson.asString(
                key, value));
    }
}
