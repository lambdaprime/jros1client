package id.jrosclient.ros.responses.transformers;

import id.jrosclient.ros.api.impl.RawResponse;
import id.jrosclient.ros.entities.Publisher;
import id.jrosclient.ros.entities.Subscriber;
import id.jrosclient.ros.entities.transformers.Transformer;
import id.jrosclient.ros.responses.SystemStateResponse;

public class SystemStateTransformer implements ResponseTransformer {

    private Transformer<Publisher> publisherTransformer;
    private Transformer<Subscriber> subscriberTransformer;

    public SystemStateTransformer(Transformer<Publisher> publisherTransformer,
            Transformer<Subscriber> subscriberTransformer) {
        this.publisherTransformer = publisherTransformer;
        this.subscriberTransformer = subscriberTransformer;
    }

    public SystemStateResponse parse(RawResponse response) {
        SystemStateResponse ss = new SystemStateResponse();
        ResponseTransformer.populate(ss, response);
        RawResponse systemState = response.get(2);
        for (RawResponse publisher: systemState.get(0)) {
            ss.publishers.add(publisherTransformer.transform(publisher.getObject()));
        }
        for (RawResponse subscriber: systemState.get(1)) {
            ss.subscribers.add(subscriberTransformer.transform(subscriber.getObject()));
        }
        return ss;
    }
    
}
