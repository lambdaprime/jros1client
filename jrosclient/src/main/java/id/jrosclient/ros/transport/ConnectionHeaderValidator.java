package id.jrosclient.ros.transport;

import java.util.logging.Level;

import id.jrosmessages.Message;
import id.jrosmessages.impl.MetadataAccessor;
import id.xfunction.logging.XLogger;

public class ConnectionHeaderValidator {

    private static final XLogger LOGGER = XLogger.getLogger(ConnectionHeaderValidator.class);
    
    private MetadataAccessor metadataAccessor;
    
    public ConnectionHeaderValidator(MetadataAccessor metadataAccessor) {
        this.metadataAccessor = metadataAccessor;
    }

    public boolean validate(Class<? extends Message> messageClass, ConnectionHeader header) {
        if (header.getType().isEmpty()) {
            LOGGER.log(Level.FINE, "Type is empty");
            return false;
        }
        var type = header.getType().get();
        
        var messageType = metadataAccessor.getType(messageClass);        
        if (!messageType.equals(type)) {
            LOGGER.fine("Message type missmatch {0} != {1}",
                    messageType, type);
            return false;
        }
        
        if (header.getMd5sum().isEmpty()) {
            LOGGER.log(Level.FINE, "MD5 sum is empty");
            return false;
        }
        var md5sum = header.getMd5sum().get();
        
        var messageMd5 = metadataAccessor.getMd5(messageClass);
        if (!messageMd5.equals(md5sum)) {
            LOGGER.fine("Message type missmatch {0} != {1}",
                    messageType, type);
            return false;
        }
        return true;
    }

}
