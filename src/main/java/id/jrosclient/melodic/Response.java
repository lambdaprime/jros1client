package id.jrosclient.melodic;

public class Response {

    public enum StatusCode {
        ERROR, FAILURE, SUCCESS
    }
    
    public StatusCode statusCode;
    public String statusMessage;

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("StatusCode: " + statusCode).append('\n');
        b.append("StatusMessage: " + statusMessage).append('\n');
        return b.toString();
    }
}
