package id.jrosclient.ros.impl.parsers;

import id.jrosclient.ros.impl.RawResponse;
import id.jrosclient.ros.responses.Publisher;

public class PublisherParser {

    public Publisher parse(RawResponse publisher) {
        return new Publisher(publisher.get(0).string(),
                publisher.get(1).list());
    }

}
