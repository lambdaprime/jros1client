package id.jrosclient.ros.transport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import id.jrosclient.TopicPublisher;
import id.jrosmessages.Message;
import id.xfunction.XRE;

public class PublishersManager {

    // topic name to its publisher
    private Map<String, TopicPublisher<?>> publishers = new HashMap<>();
    
    public <M extends Message> void add(TopicPublisher<M> publisher) {
        if (publishers.containsKey(publisher.getTopic()))
            throw new XRE("Publisher for topic %s already exist");
        publishers.put(publisher.getTopic(), publisher);
    }
    
    public Optional<TopicPublisher<?>> getPublisher(String topic) {
        return Optional.ofNullable(publishers.get(topic));
    }

    public void remove(String topic) {
        publishers.remove(topic);
    }
    
    public List<TopicPublisher<?>> getPublishers() {
        return List.copyOf(publishers.values());
    }
}
