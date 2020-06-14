package id.jrosmessages;

import java.util.HashMap;
import java.util.Map;

import id.jrosmessages.geometry_msgs.PointMessage;
import id.jrosmessages.geometry_msgs.PoseMessage;
import id.jrosmessages.geometry_msgs.QuaternionMessage;
import id.jrosmessages.geometry_msgs.Vector3Message;
import id.jrosmessages.std_msgs.ColorRGBAMessage;
import id.jrosmessages.std_msgs.HeaderMessage;
import id.jrosmessages.std_msgs.StringMessage;

public class MessagesDirectory {

    private final Map<String, Class<?>> messages = new HashMap<>();

    public MessagesDirectory() {
        add(StringMessage.class);
        add(PointMessage.class);
        add(QuaternionMessage.class);
        add(PoseMessage.class);
        add(HeaderMessage.class);
        add(ColorRGBAMessage.class);
        add(Vector3Message.class);
    }
    
    public Class<?> get(String messageType) {
        return messages.get(messageType);
    }
    
    public String getMd5(Class<?> messageClass) {
        return messageClass.getAnnotation(Message.class).md5sum();
    }
    
    public void add(Class<?> messageClass) {
        messages.put(messageClass.getAnnotation(Message.class).type(), messageClass);
    }
}
