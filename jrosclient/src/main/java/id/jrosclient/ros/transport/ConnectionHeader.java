package id.jrosclient.ros.transport;

import java.util.Optional;

import id.xfunction.XJson;

public class ConnectionHeader {

    public static final String CALLER_ID = "callerid";
    public static final String TOPIC = "topic";
    public static final String TYPE = "type";
    public static final String MESSAGE_DEFINITION = "message_definition";
    public static final String MD5_SUM = "md5sum";

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

    public void add(String key, String value) {
        switch (key) {
        case CALLER_ID: withCallerId(value); break;
        case TOPIC: withTopic(value); break;
        case TYPE: withType(value); break;
        case MESSAGE_DEFINITION: withMessageDefinition(value); break;
        case MD5_SUM: withMd5Sum(value); break;
        }
    }

    @Override
    public String toString() {
        return XJson.asString(
            CALLER_ID, callerId.orElse("empty"),
            TOPIC, topic.orElse("empty"),
            TYPE, type.orElse("empty"),
            MESSAGE_DEFINITION, messageDefinition.orElse("empty"),
            MD5_SUM, md5sum.orElse("empty"));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (this.getClass() != obj.getClass())
            return false;
        ConnectionHeader ch = (ConnectionHeader) obj;
        return callerId.equals(ch.callerId)
                && topic.equals(ch.topic)
                && type.equals(ch.type)
                && messageDefinition.equals(ch.messageDefinition)
                && md5sum.equals(ch.md5sum);
    }
}
