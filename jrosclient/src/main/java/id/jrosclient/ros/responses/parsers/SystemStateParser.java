package id.jrosclient.ros.responses.parsers;

import id.jrosclient.ros.api.impl.RawResponse;
import id.jrosclient.ros.entities.Publisher;
import id.jrosclient.ros.entities.transformers.Transformer;
import id.jrosclient.ros.responses.SystemStateResponse;

public class SystemStateParser extends ResponseTransformer {

    private Transformer<Publisher> publisherTransformer;

    public SystemStateParser(Transformer<Publisher> publisherTransformer) {
        this.publisherTransformer = publisherTransformer;
    }

    public SystemStateResponse parse(RawResponse response) {
        SystemStateResponse ss = new SystemStateResponse();
        populate(ss, response);
        RawResponse systemState = response.get(2);
        for (RawResponse publisher: systemState.get(0)) {
            ss.publishers.add(publisherTransformer.transform(publisher.getObject()));
        }
        return ss;
    }
    
}
