package id.jrosclient;

import java.util.concurrent.Flow;

import id.jrosmessages.Message;

/**
 * <p>Publisher responsible for publishing messages for particular topic.
 * Other ROS nodes can see the topic and subscribe for it so they will
 * start receiving messages published to it.</p>
 * 
 * <p><b>JRosClient</b> provides default implementation for publisher which can
 * be used in most of the cases.</p>
 * 
 * @param <M> type of messages in the topic
 */
public interface TopicPublisher<M extends Message> extends Flow.Publisher<M>, AutoCloseable {

    /**
     * @return class of messages which is published in this topic
     */
    Class<M> getMessageClass();
    
    /**
     * @return Topic name
     */
    String getTopic();

    /**
     * Stop to emit any new messages
     */
    void close() throws Exception;
}
