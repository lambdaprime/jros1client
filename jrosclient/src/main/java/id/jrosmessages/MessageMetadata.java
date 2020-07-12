package id.jrosmessages;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Every Message object suppose to contain message data only and 
 * not its metadata.
 * 
 * That is why there is no common Message iface with all metadata
 * methods. Doing so you would be able to access Message metadata
 * only thru Message objects.
 * 
 * Using annotation on other hand allows to keep all Message metadata in its
 * class object and access it right from there and avoid unnecessary
 * Message object instantiation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageMetadata {
    
    /**
     * ROS message type name.
     * 
     * For example: std_msgs/String
     */
    String type();
    
    /**
     * <p>MD5 sum of the message. You can calculate it using
     * rosmsg command:</p>
     * 
     * <pre>rosmsg md5 std_msgs/String</pre>
     * 
     */
    String md5sum();
}
