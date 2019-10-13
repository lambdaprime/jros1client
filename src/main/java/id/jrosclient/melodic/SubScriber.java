package id.jrosclient.melodic;

import java.util.List;

public class SubScriber {

    public String topic;
    public List<String> topicSubscriber;

    @Override
    public String toString() {
        return String.format("{ \"topic\": \"%s\", \"topicSubscriber\": %s }", topic,
                Utils.asArrayOfStrings(topicSubscriber));
    }
}
