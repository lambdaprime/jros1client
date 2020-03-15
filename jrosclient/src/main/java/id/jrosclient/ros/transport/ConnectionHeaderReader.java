package id.jrosclient.ros.transport;

import java.io.IOException;
import java.util.HashMap;

public class ConnectionHeaderReader {

    private RosDataInput in;

    public ConnectionHeaderReader(RosDataInput in) {
        this.in = in;
    }

    public ConnectionHeader read() throws IOException {
        int len = in.readLen();
        var map = new HashMap<String, String>();
        while (len != 0) {
            int fieldLen = in.readLen();
            var field = in.readField(fieldLen);
            map.put(field.getKey(), field.getValue());
            len -= 4 + fieldLen;
        }
        var ch = new ConnectionHeader();
        map.entrySet().stream()
            .forEach(e -> ch.add(e.getKey(), e.getValue()));
        return ch;
    }

}
