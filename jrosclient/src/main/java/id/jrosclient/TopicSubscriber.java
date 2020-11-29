package id.jrosclient;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;

import id.jrosmessages.Message;
import id.xfunction.XUtils;

/**
 * <p>Subscriber receives messages for the topic it is
 * subscribed for and passes them to the onNext method.</p>
 * 
 * <p>This class simplifies implementation of new subscribers since it
 * provides default implementations for most methods except onNext.
 * The onNext method needs to be implemented for each subscriber
 * separately.</p>
 * 
 * <p>Before subscriber can start to receive messages it needs
 * to be subscribed for the topic.</p>
 * 
 * <p>See <a href="{@docRoot}/index.html">Module documentation</a> for examples.</p>
 * 
 * @param <M> type of messages in the topic
 */
public abstract class TopicSubscriber<M extends Message> implements Flow.Subscriber<M> {

    private Class<M> messageClass;
    private Subscription subscription;
    private String topic;
    
    /**
     * @param messageClass class of the messages in this topic
     * @param topic Name of the topic which messages current subscriber wants to receive.
     * Topic name which should start from '/'
     */
    public TopicSubscriber(Class<M> messageClass, String topic) {
        this.messageClass = messageClass;
        this.topic = topic;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        XUtils.printExceptions(throwable);
    }

    /**
     * Default implementation does nothing but it can be redefined.
     */
    @Override
    public void onComplete() {
        
    }

    /**
     * Class of the messages in the current topic
     */
    public Class<M> getMessageClass() {
        return messageClass;
    }

    public Subscription getSubscription() {
        return subscription;
    }
    
    /**
     * Name of the topic of this subscriber
     */
    public String getTopic() {
        return topic;
    }
    
    /**
     * Request next n new messages available for the current topic
     * 
     * @param n number of messages to request
     */
    public void request(int n) {
        subscription.request(n);
    }
}
