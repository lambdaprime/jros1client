package id.jrosclient.ros.entities;

import java.util.List;

import id.jrosclient.ros.api.impl.Utils;

public class Publisher implements Entity {

    public String topic;
    public List<String> topicPublisher;
    
    public Publisher(String topic, List<String> topicPublisher) {
        this.topic = topic;
        this.topicPublisher = topicPublisher;
    }

    @Override
    public String toString() {
        return String.format("{ \"topic\": \"%s\", \"topicPublisher\": %s }", topic,
                Utils.asArrayOfStrings(topicPublisher));
    }
}
