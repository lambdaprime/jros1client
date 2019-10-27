package id.jrosclient.ros.responses;

public class Response {

    public enum StatusCode {
        ERROR, FAILURE, SUCCESS
    }
    
    public StatusCode statusCode;
    public String statusMessage;

    @Override
    public String toString() {
        return String.format("\"statusCode\": \"%s\", \"statusMessage\": \"%s\"",
                statusCode, statusMessage);
    }
}
