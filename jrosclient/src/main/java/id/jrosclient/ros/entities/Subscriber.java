package id.jrosclient.ros.entities;

import java.util.List;

import id.xfunction.XJson;

public class Subscriber implements Entity {

    public String topic;
    public List<String> topicSubscriber;

    public Subscriber(String topic, List<String> topicPublisher) {
        this.topic = topic;
        this.topicSubscriber = topicPublisher;
    }
    
    @Override
    public String toString() {
        return XJson.asString("topic", topic,
                "topicSubscriber", topicSubscriber);
    }
}
