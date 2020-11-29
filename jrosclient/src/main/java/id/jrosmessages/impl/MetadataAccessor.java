package id.jrosmessages.impl;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;

/**
 * Allows to access message metadata based on their class object.
 */
public class MetadataAccessor {
    
    public String getMd5(Class<? extends Message> messageClass) {
        return messageClass.getAnnotation(MessageMetadata.class).md5sum();
    }
    
    public String getType(Class<? extends Message> messageClass) {
        return messageClass.getAnnotation(MessageMetadata.class).type();
    }

}
