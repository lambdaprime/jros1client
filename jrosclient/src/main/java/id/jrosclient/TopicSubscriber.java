package id.jrosclient;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;

import id.jrosmessages.Message;
import id.xfunction.XUtils;

public abstract class TopicSubscriber<M extends Message> implements Flow.Subscriber<M> {

    private Class<M> messageClass;
    private Subscription subscription;
    private String topic;
    
    /**
     * @param messageClass class of the messages in this topic
     * @param topic topic name which should always start from '/' or
     * it will be added automatically
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

    @Override
    public void onComplete() {
        
    }

    public Class<M> getMessageClass() {
        return messageClass;
    }

    public Subscription getSubscription() {
        return subscription;
    }
    
    public String getTopic() {
        return topic;
    }
    
    public void request(int n) {
        subscription.request(n);
    }
}
