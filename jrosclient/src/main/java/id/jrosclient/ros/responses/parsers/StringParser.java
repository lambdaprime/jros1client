package id.jrosclient.ros.responses.parsers;

import id.jrosclient.ros.api.impl.RawResponse;
import id.jrosclient.ros.responses.StringResponse;

public class StringParser extends ResponseTransformer {

    public StringResponse parse(String key, RawResponse response) {
        StringResponse ss = new StringResponse(key);
        populate(ss, response);
        ss.value = response.get(2).string();
        return ss;
    }
    
}
