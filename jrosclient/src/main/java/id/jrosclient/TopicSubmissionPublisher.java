package id.jrosclient;

import java.util.concurrent.SubmissionPublisher;

import id.jrosmessages.Message;
import id.xfunction.XJson;

public class TopicSubmissionPublisher<M extends Message> extends SubmissionPublisher<M> 
    implements TopicPublisher<M>
{

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
    
    @Override
    public String toString() {
        return XJson.asString("topic", topic);
    }
}
