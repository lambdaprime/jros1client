package id.jrosmessages;

/**
 * Allows to access Message's metadata
 * 
 */
public class MetadataAccessor {
    
    public String getMd5(Class<?> messageClass) {
        return messageClass.getAnnotation(Message.class).md5sum();
    }
    
    public String getType(Class<?> messageClass) {
        return messageClass.getAnnotation(Message.class).type();
    }

}
