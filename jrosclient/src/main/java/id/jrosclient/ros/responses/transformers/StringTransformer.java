package id.jrosclient.ros.responses.transformers;

import id.jrosclient.ros.api.impl.RawResponse;
import id.jrosclient.ros.responses.StringResponse;

public class StringTransformer implements ResponseTransformer {

    public StringResponse parse(String key, RawResponse response) {
        StringResponse ss = new StringResponse(key);
        ResponseTransformer.populate(ss, response);
        ss.value = response.get(2).string();
        return ss;
    }
    
}
