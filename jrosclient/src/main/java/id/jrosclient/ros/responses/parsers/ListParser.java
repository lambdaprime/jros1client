package id.jrosclient.ros.responses.parsers;

import id.jrosclient.ros.impl.RawResponse;
import id.jrosclient.ros.responses.ListResponse;
import id.jrosclient.ros.responses.Response.StatusCode;

public class ListParser extends ResponseParser {

    public ListResponse<String> parseString(String key, RawResponse response) {
        ListResponse<String> ss = new ListResponse<>(key);
        populate(ss, response);
        if (ss.statusCode == StatusCode.ERROR) {
            return ss;
        }
        for (RawResponse r: response.get(2)) {
            ss.value.add(r.string());
        }
        return ss;
    }

}
