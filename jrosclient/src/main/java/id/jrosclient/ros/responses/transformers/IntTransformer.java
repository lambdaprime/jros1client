package id.jrosclient.ros.responses.transformers;

import id.jrosclient.ros.api.impl.RawResponse;
import id.jrosclient.ros.responses.IntResponse;

public class IntTransformer implements ResponseTransformer {

    public IntResponse parse(String key, RawResponse response) {
        IntResponse ss = new IntResponse(key);
        ResponseTransformer.populate(ss, response);
        ss.value = response.get(2).integer();
        return ss;
    }
    
}
