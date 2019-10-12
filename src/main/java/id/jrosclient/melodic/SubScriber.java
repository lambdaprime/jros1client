package id.jrosclient.melodic;

import java.util.List;

public class SubScriber {

    public String topic;
    public List<String> topicSubscriber;

    @Override
    public String toString() {
        return String.format("SubScriber: %s [%s]", topic, topicSubscriber);
    }
}
