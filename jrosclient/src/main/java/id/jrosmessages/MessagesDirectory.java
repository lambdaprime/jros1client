package id.jrosmessages;

import java.util.HashMap;
import java.util.Map;

import id.jrosmessages.std_msgs.StringMessage;

public class MessagesDirectory {

    private final Map<String, Class<?>> messages = new HashMap<>();

    public MessagesDirectory() {
        add(StringMessage.class);
    }
    
    public Map<String, Class<?>> get() {
        return messages;
    }

    public void add(Class<?> clazz) {
        messages.put(clazz.getAnnotation(Message.class).type(), clazz);
    }
}
