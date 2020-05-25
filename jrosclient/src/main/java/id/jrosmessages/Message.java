package id.jrosmessages;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Message {
    
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
