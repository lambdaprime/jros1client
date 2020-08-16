package id.jrosclient.ros.transport.io;

import java.io.DataInput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import id.jrosclient.ros.transport.ConnectionHeader;

public class ConnectionHeaderReader {

    private DataInput in;
    private Utils utils = new Utils();

    public ConnectionHeaderReader(DataInput in) {
        this.in = in;
    }

    public ConnectionHeader read() throws IOException {
        int len = utils.readLen(in);
        var map = new HashMap<String, String>();
        while (len != 0) {
            int fieldLen = utils.readLen(in);
            var field = readField(fieldLen);
            map.put(field.getKey(), field.getValue());
            len -= 4 + fieldLen;
        }
        var ch = new ConnectionHeader();
        map.entrySet().stream()
            .forEach(e -> ch.add(e.getKey(), e.getValue()));
        return ch;
    }

    private Entry<String, String> readField(int fieldLen) throws IOException {
        byte[] buf = new byte[fieldLen];
        in.readFully(buf);
        var a = new String(buf).split("=");
        return Map.entry(a[0], a[1]);
    }

}
