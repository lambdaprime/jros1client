package id.jrosclient.ros.responses.transformers;

import id.jrosclient.ros.api.impl.RawResponse;
import id.jrosclient.ros.responses.Response;
import id.jrosclient.ros.responses.Response.StatusCode;

public interface ResponseTransformer {

    static void populate(Response dst, RawResponse src) {
        dst.statusCode = StatusCode.valueOf(src.get(0).integer());
        dst.statusMessage = src.get(1).string();
    }

    static void populate(RawResponse dst, Response src) {
        dst.list().set(0, src.statusCode.code());
        dst.list().set(1, src.statusMessage);
    }

}
