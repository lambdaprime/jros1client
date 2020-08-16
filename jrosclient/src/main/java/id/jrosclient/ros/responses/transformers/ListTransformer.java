package id.jrosclient.ros.responses.transformers;

import id.jrosclient.ros.api.impl.RawResponse;
import id.jrosclient.ros.responses.ListResponse;
import id.jrosclient.ros.responses.Response.StatusCode;

public class ListTransformer implements ResponseTransformer {

    public ListResponse<String> parseString(String key, RawResponse response) {
        ListResponse<String> ss = new ListResponse<>(key);
        ResponseTransformer.populate(ss, response);
        if (ss.statusCode == StatusCode.ERROR) {
            return ss;
        }
        for (RawResponse r: response.get(2)) {
            ss.value.add(r.string());
        }
        return ss;
    }

}
