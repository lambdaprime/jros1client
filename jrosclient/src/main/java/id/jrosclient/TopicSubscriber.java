package id.jrosclient;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;
import java.util.logging.Logger;

import id.jrosmessages.Message;
import id.xfunction.XUtils;
import id.xfunction.logging.XLogger;

public abstract class TopicSubscriber<M extends Message> implements Flow.Subscriber<M> {

    private static final Logger LOGGER = XLogger.getLogger(TopicSubscriber.class);
    private Class<M> messageClass;
    private Subscription subscription;
    
    public TopicSubscriber(Class<M> messageClass) {
        this.messageClass = messageClass;
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
}
