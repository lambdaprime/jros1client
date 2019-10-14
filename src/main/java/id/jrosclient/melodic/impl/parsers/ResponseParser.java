package id.jrosclient.melodic.impl.parsers;

import id.jrosclient.RawResponse;
import id.jrosclient.melodic.responses.Response;
import id.jrosclient.melodic.responses.Response.StatusCode;

public class ResponseParser {

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
