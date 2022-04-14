/*
 * Copyright 2020 jrosclient project
 * 
 * Website: https://github.com/lambdaprime/jros1client
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package id.jros1client.ros.transport;

import id.xfunction.XJson;
import id.xfunction.logging.XLogger;
import java.util.Optional;

/**
 * <a href=
 * "http://wiki.ros.org/ROS/Connection%20Header">http://wiki.ros.org/ROS/Connection%20Header</a>
 */
/** @author lambdaprime intid@protonmail.com */
public class ConnectionHeader {

    private static final XLogger LOGGER = XLogger.getLogger(ConnectionHeader.class);

    public static final String CALLER_ID = "callerid";
    public static final String TOPIC = "topic";
    public static final String TYPE = "type";
    public static final String MESSAGE_DEFINITION = "message_definition";
    public static final String MD5_SUM = "md5sum";
    public static final String LATCHING = "latching";

    /**
     * Name of node sending data.
     *
     * <p>Required to be set by the subscriber. It is recommended to set it by publisher as well for
     * debugging purposes.
     */
    public Optional<String> callerId = Optional.empty();

    /**
     * Name of the topic the subscriber is connecting to
     *
     * <p>Required to be set by the subscriber.
     */
    public Optional<String> topic = Optional.empty();

    /**
     * Message type.
     *
     * <p>Required to be set by the subscriber. Required to be set by the publisher.
     */
    public Optional<String> type = Optional.empty();

    /**
     * Md5sum of the message type
     *
     * <p>Required to be set by the subscriber. Required to be set by the publisher.
     */
    public Optional<String> md5sum = Optional.empty();

    /**
     * Enables "latching" on a connection. When a connection is latched, the last message published
     * is saved and automatically sent to any future subscribers that connect. This is useful for
     * slow-changing to static data like a map.
     *
     * <p>Optional by the publisher/subscriber.
     */
    public Optional<String> latching = Optional.empty();

    /** Required to be set by the subscriber. */
    public Optional<String> messageDefinition = Optional.empty();

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

    public ConnectionHeader withLatching(boolean value) {
        this.latching = Optional.of(value ? "1" : "0");
        return this;
    }

    public void add(String key, String value) {
        switch (key) {
            case CALLER_ID:
                withCallerId(value);
                break;
            case TOPIC:
                withTopic(value);
                break;
            case TYPE:
                withType(value);
                break;
            case MESSAGE_DEFINITION:
                withMessageDefinition(value);
                break;
            case MD5_SUM:
                withMd5Sum(value);
                break;
            case LATCHING:
                withLatching(Integer.parseInt(value) == 1);
                break;
            default:
                LOGGER.warning("Received unknown Connection Header field: {0} = {1} ", key, value);
        }
    }

    public Optional<String> getMd5sum() {
        return md5sum;
    }

    public Optional<String> getType() {
        return type;
    }

    public Optional<String> getCallerId() {
        return callerId;
    }

    public Optional<String> getTopic() {
        return topic;
    }

    public Optional<String> getLatching() {
        return latching;
    }

    @Override
    public String toString() {
        return XJson.asString(
                CALLER_ID, callerId.orElse("empty"),
                TOPIC, topic.orElse("empty"),
                TYPE, type.orElse("empty"),
                MESSAGE_DEFINITION, messageDefinition.orElse("empty"),
                MD5_SUM, md5sum.orElse("empty"),
                LATCHING, latching.orElse("empty"));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        ConnectionHeader ch = (ConnectionHeader) obj;
        return callerId.equals(ch.callerId)
                && topic.equals(ch.topic)
                && type.equals(ch.type)
                && messageDefinition.equals(ch.messageDefinition)
                && md5sum.equals(ch.md5sum)
                && latching.equals(ch.latching);
    }
}
