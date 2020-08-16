package id.jrosclient.ros.responses.parsers;

import id.jrosclient.ros.api.impl.RawResponse;
import id.jrosclient.ros.responses.Response;
import id.jrosclient.ros.responses.Response.StatusCode;

public class ResponseTransformer {

    protected void populate(Response response, RawResponse rawResponse) {
        switch (rawResponse.get(0).integer()) {
        case -1: response.statusCode = StatusCode.ERROR; break;
        case 0: response.statusCode = StatusCode.FAILURE; break;
        case 1: response.statusCode = StatusCode.SUCCESS; break;
        default: throw new RuntimeException();
        }
        response.statusMessage = rawResponse.get(1).string();
    }
}
