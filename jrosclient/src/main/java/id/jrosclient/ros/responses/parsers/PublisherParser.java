package id.jrosclient.ros.responses.parsers;

import id.jrosclient.ros.entities.Publisher;
import id.jrosclient.ros.impl.RawResponse;

public class PublisherParser {

    public Publisher parse(RawResponse publisher) {
        return new Publisher(publisher.get(0).string(),
                publisher.get(1).list());
    }

}
