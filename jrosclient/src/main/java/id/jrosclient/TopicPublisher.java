package id.jrosclient;

import java.util.concurrent.Flow;

import id.jrosmessages.Message;

public interface TopicPublisher<M extends Message> extends Flow.Publisher<M> {

    /**
     * @return class of messages which are published
     */
    Class<M> getMessageClass();
    
    /**
     * @return Topic name
     */
    String getTopic() ;
}
