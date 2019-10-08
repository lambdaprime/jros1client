package id.jrosclient.melodic;

public class Response {

    public enum StatusCode {
        ERROR, FAILURE, SUCCESS
    }
    
    public StatusCode statusCode;
    public String statusMessage;
}
