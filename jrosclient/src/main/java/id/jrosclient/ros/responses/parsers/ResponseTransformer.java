package id.jrosclient.ros.responses.parsers;

import id.jrosclient.ros.api.impl.RawResponse;
import id.jrosclient.ros.responses.Response;
import id.jrosclient.ros.responses.Response.StatusCode;

public class ResponseTransformer {

    protected void populate(Response dst, RawResponse src) {
        dst.statusCode = StatusCode.valueOf(src.get(0).integer());
        dst.statusMessage = src.get(1).string();
    }

    protected void populate(RawResponse dst, Response src) {
        dst.list().set(0, src.statusCode.code());
        dst.list().set(1, src.statusMessage);
    }

}
