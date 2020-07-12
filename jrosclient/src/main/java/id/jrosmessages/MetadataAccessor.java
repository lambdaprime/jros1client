package id.jrosmessages;

/**
 * Allows to access Message's metadata
 * 
 */
public class MetadataAccessor {
    
    public String getMd5(Class<? extends Message> messageClass) {
        return messageClass.getAnnotation(MessageMetadata.class).md5sum();
    }
    
    public String getType(Class<? extends Message> messageClass) {
        return messageClass.getAnnotation(MessageMetadata.class).type();
    }

}
