package id.jrosclient.ros.responses;

import java.util.List;

import id.jrosclient.ros.impl.Utils;

public class SubScriber {

    public String topic;
    public List<String> topicSubscriber;

    @Override
    public String toString() {
        return String.format("{ \"topic\": \"%s\", \"topicSubscriber\": %s }", topic,
                Utils.asArrayOfStrings(topicSubscriber));
    }
}
