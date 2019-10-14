package id.jrosclient.melodic.impl.parsers;

import id.jrosclient.RawResponse;
import id.jrosclient.melodic.responses.StringResponse;

public class StringParser extends ResponseParser {

    public StringResponse parse(String key, RawResponse response) {
        StringResponse ss = new StringResponse(key);
        populate(ss, response);
        ss.value = response.get(2).string();
        return ss;
    }
    
}
