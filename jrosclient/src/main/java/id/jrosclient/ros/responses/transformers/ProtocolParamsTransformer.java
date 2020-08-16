package id.jrosclient.ros.responses.transformers;

import id.jrosclient.ros.api.impl.RawResponse;
import id.jrosclient.ros.responses.ProtocolParamsResponse;

public class ProtocolParamsTransformer implements ResponseTransformer {

    public ProtocolParamsResponse parse(RawResponse response) {
        ProtocolParamsResponse ss = new ProtocolParamsResponse();
        ResponseTransformer.populate(ss, response);
        RawResponse protocolParams = response.get(2);
        ss.name = protocolParams.get(0).string();
        ss.host = protocolParams.get(1).string();
        ss.port = protocolParams.get(2).integer();
        return ss;
    }
    
    public RawResponse transform(ProtocolParamsResponse response) {
        var raw = new RawResponse(new Object[3]);
        ResponseTransformer.populate(raw, response);
        raw.list().set(2, new Object[]{response.name, response.host, response.port});
        return raw;
    }
}
