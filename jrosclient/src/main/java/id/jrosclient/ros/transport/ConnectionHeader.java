package id.jrosclient.ros.transport;

import java.util.Optional;

public class ConnectionHeader {

    public Optional<String> callerId = Optional.empty();
    public Optional<String> topic = Optional.empty();
    public Optional<String> type = Optional.empty();
    public Optional<String> messageDefinition = Optional.empty();
    public Optional<String> md5sum = Optional.empty();

    public ConnectionHeader withCallerId(String callerId) {
        this.callerId = Optional.of(callerId);
        return this;
    }

    public ConnectionHeader withTopic(String topic) {
        this.topic = Optional.of(topic);
        return this;
    }

    public ConnectionHeader withType(String type) {
        this.type = Optional.of(type);
        return this;
    }

    public ConnectionHeader withMessageDefinition(String messageDefinition) {
        this.messageDefinition = Optional.of(messageDefinition);
        return this;
    }

    public ConnectionHeader withMd5Sum(String md5sum) {
        this.md5sum = Optional.of(md5sum);
        return this;
    }

}
