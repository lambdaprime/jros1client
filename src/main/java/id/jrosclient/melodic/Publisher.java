package id.jrosclient.melodic;

import java.util.List;

public class Publisher {

    public String topic;
    public List<String> topicPublisher;

    
    public Publisher(String topic, List<String> topicPublisher) {
        this.topic = topic;
        this.topicPublisher = topicPublisher;
    }


    @Override
    public String toString() {
        return String.format("Publisher: %s [%s]", topic, topicPublisher);
    }
}
