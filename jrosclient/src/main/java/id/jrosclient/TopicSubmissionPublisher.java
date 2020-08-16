package id.jrosclient;

import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Logger;

import id.jrosmessages.Message;
import id.xfunction.logging.XLogger;

public class TopicSubmissionPublisher<M extends Message> extends SubmissionPublisher<M> implements TopicPublisher<M>
{

    private static final Logger LOGGER = XLogger.getLogger(TopicSubmissionPublisher.class);
    private Class<M> messageClass;
    private String topic;
    
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
}
