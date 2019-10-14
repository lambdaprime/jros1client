package id.jrosclient.melodic.impl.parsers;

import id.jrosclient.RawResponse;
import id.jrosclient.melodic.responses.ListResponse;
import id.jrosclient.melodic.responses.Response.StatusCode;

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
