package id.jrosclient.melodic.responses;

import java.util.ArrayList;
import java.util.List;

import id.jrosclient.melodic.Utils;

public class ListResponse<T> extends Response {

    public final String key;
    public List<T> value = new ArrayList<>();

    public ListResponse(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return String.format("{%s, \"%s\": %s}", super.toString(),
                key, Utils.asArrayOfStrings(value));
    }

}
