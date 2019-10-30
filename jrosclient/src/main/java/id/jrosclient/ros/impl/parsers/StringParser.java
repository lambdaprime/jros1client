package id.jrosclient.ros.impl.parsers;

import id.jrosclient.ros.impl.RawResponse;
import id.jrosclient.ros.responses.StringResponse;

public class StringParser extends ResponseParser {

    public StringResponse parse(String key, RawResponse response) {
        StringResponse ss = new StringResponse(key);
        populate(ss, response);
        ss.value = response.get(2).string();
        return ss;
    }
    
}
