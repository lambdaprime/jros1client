package id.jrosclient.ros.responses;

import id.xfunction.XJson;

/**
 * Base class for all ROS API returned values.
 * 
 * @see <a href="http://wiki.ros.org/ROS/Master_Slave_APIs">Return value format</a>
 */
public class Response {

    public enum StatusCode {
        ERROR(-1), FAILURE(0), SUCCESS(1);
        
        private int code;
        
        public int code() { return code; };
        
        StatusCode(int code) { this.code = code; }
        
        public static StatusCode valueOf(int code) {
            switch (code) {
            case -1: return StatusCode.ERROR;
            case 0: return StatusCode.FAILURE;
            case 1: return StatusCode.SUCCESS;
            default: throw new RuntimeException();
            }
        }
    }
    
    public StatusCode statusCode;
    public String statusMessage;

    public Response withStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public Response withStatusMessage(String message) {
        this.statusMessage = message;
        return this;
    }

    @Override
    public String toString() {
        return XJson.asString(
                "statusCode", statusCode,
                "statusMessage", statusMessage);
    }
}
