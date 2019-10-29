package id.jrosclient.ros.responses;

import java.util.List;

import id.jrosclient.ros.impl.Utils;

public class Publisher {

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
