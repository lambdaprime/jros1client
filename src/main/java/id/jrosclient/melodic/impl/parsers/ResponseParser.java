package id.jrosclient.melodic.impl.parsers;

import id.jrosclient.melodic.Response;
import id.jrosclient.melodic.Response.StatusCode;

public class ResponseParser {

    protected void populate(Response response, Object[] rawResponse) {
        switch ((Integer)rawResponse[0]) {
        case -1: response.statusCode = StatusCode.ERROR; break;
        case 0: response.statusCode = StatusCode.FAILURE; break;
        case 1: response.statusCode = StatusCode.SUCCESS; break;
        default: throw new RuntimeException();
        }
        response.statusMessage = (String)rawResponse[1];
    }
}
