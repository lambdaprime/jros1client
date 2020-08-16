package id.jrosclient.ros.transport;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import id.ICE.scanners.MessageScanner;
import id.jrosclient.ros.transport.io.Utils;
import id.xfunction.io.ByteBufferInputStream;

public class ConnectionHeaderScanner implements MessageScanner {

    private Utils utils = new Utils();
    
    @Override
    public int scan(ByteBuffer buffer) {
        try {
            if (buffer.limit() < 4) return -1;
            var dis = new DataInputStream(new ByteBufferInputStream(buffer));
            var len = utils.readLen(dis) + 4;
            if (buffer.limit() < len) return -1;
            return len;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
