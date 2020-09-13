package id.jrosclient.ros.transport;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import id.ICE.scanners.MessageScanner;
import id.jrosclient.ros.transport.io.Utils;
import id.xfunction.io.ByteBufferInputStream;
import id.xfunction.logging.XLogger;

public class ConnectionHeaderScanner implements MessageScanner {

    private static final XLogger LOGGER = XLogger.getLogger(ConnectionHeaderScanner.class);
    private Utils utils = new Utils();
    
    @Override
    public int scan(ByteBuffer buffer) {
        LOGGER.entering("scan");
        int ret = scanInternal(buffer);
        LOGGER.exiting("scan", ret);
        return ret;
    }

    private int scanInternal(ByteBuffer buffer) {
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
