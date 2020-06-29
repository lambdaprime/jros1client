package id.jrosclient.ros.responses;

import id.xfunction.XJson;

public class Response {

    public enum StatusCode {
        ERROR, FAILURE, SUCCESS
    }
    
    public StatusCode statusCode;
    public String statusMessage;

    @Override
    public String toString() {
        return XJson.asString(
                "statusCode", statusCode,
                "statusMessage", statusMessage);
    }
}
