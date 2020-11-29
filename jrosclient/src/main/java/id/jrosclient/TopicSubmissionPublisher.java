package id.jrosclient;

import java.util.concurrent.SubmissionPublisher;

import id.jrosmessages.Message;
import id.xfunction.XJson;

/**
 * <p>Topic publisher which is based on Java class {@link java.util.concurrent.SubmissionPublisher}.</p>
 * 
 * <p>It asynchronously issues submitted messages to current subscribers.
 * All subscribers receive messages in the same order.</p>
 * 
 * <p>By default for delivery to subscribers ForkJoinPool.commonPool() is used.
 * Each subscriber uses an independent queue.</p>
 * 
 * <p>If there is no subscribers available for a given topic then all published
 * messages are discarded.</p>
 * 
 * <p>See <a href="{@docRoot}/index.html">Module documentation</a> for examples.</p>
 * 
 */
public class TopicSubmissionPublisher<M extends Message> extends SubmissionPublisher<M> 
    implements TopicPublisher<M>
{

    private Class<M> messageClass;
    private String topic;

    /**
     * @param messageClass class of messages in the topic
     * @param topic topic name
     */
    public TopicSubmissionPublisher(Class<M> messageClass, String topic) {
        this.messageClass = messageClass;
        this.topic = topic;
    }
    
    public Class<M> getMessageClass() {
        return messageClass;
    }
    
    public String getTopic() {
        return topic;
    }
    
    @Override
    public String toString() {
        return XJson.asString("topic", topic);
    }

}
