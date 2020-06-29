package id.jrosclient.ros.responses;

import java.util.ArrayList;
import java.util.List;

import id.xfunction.XJson;

public class ListResponse<T> extends Response {

    public final String key;
    public List<T> value = new ArrayList<>();

    public ListResponse(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return XJson.merge(super.toString(), XJson.asString(
                key, value));
    }

}
