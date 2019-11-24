package id.jrosclient.ros.responses.parsers;

import id.jrosclient.ros.entities.transformers.PublisherTransformer;
import id.jrosclient.ros.impl.RawResponse;
import id.jrosclient.ros.responses.SystemStateResponse;

public class SystemStateParser extends ResponseParser {

    private PublisherTransformer publisherTransformer = new PublisherTransformer();

    public SystemStateParser(PublisherTransformer publisherTransformer) {
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
