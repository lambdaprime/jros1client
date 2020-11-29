package id.jrosclient.app;

import java.util.HashMap;
import java.util.Map;

import id.jrosmessages.Message;
import id.jrosmessages.geometry_msgs.PointMessage;
import id.jrosmessages.geometry_msgs.PoseMessage;
import id.jrosmessages.geometry_msgs.QuaternionMessage;
import id.jrosmessages.geometry_msgs.Vector3Message;
import id.jrosmessages.impl.MetadataAccessor;
import id.jrosmessages.std_msgs.ColorRGBAMessage;
import id.jrosmessages.std_msgs.HeaderMessage;
import id.jrosmessages.std_msgs.Int32Message;
import id.jrosmessages.std_msgs.StringMessage;
import id.jrosmessages.visualization_msgs.MarkerMessage;

/**
 * Directory of all message types available in the system.
 * It contains all predefined message types but it is possible to
 * add new ones.
 */
public class MessagesDirectory {

    private MetadataAccessor accessor = new MetadataAccessor();
    private final Map<String, Class<? extends Message>> messages = new HashMap<>();

    public MessagesDirectory() {
        add(StringMessage.class);
        add(PointMessage.class);
        add(QuaternionMessage.class);
        add(PoseMessage.class);
        add(HeaderMessage.class);
        add(ColorRGBAMessage.class);
        add(Vector3Message.class);
        add(MarkerMessage.class);
        add(Int32Message.class);
    }
    
    public Class<? extends Message> get(String messageType) {
        return messages.get(messageType);
    }
    
    public void add(Class<? extends Message> messageClass) {
        messages.put(accessor.getType(messageClass), messageClass);
    }
}
