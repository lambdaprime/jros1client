package id.jrosclient.ros.responses.parsers;

import id.jrosclient.ros.impl.RawResponse;
import id.jrosclient.ros.responses.ProtocolParamsResponse;

public class ProtocolParamsParser extends ResponseParser {

    public ProtocolParamsResponse parse(RawResponse response) {
        ProtocolParamsResponse ss = new ProtocolParamsResponse();
        populate(ss, response);
        RawResponse protocolParams = response.get(2);
        ss.name = protocolParams.get(0).string();
        ss.host = protocolParams.get(1).string();
        ss.port = protocolParams.get(2).integer();
        return ss;
    }
    
}
