package id.jrosclient.melodic.impl.parsers;

import id.jrosclient.RawResponse;
import id.jrosclient.melodic.Publisher;

public class PublisherParser {

    public Publisher parse(RawResponse publisher) {
        return new Publisher(publisher.get(0).string(),
                publisher.get(1).list());
    }

}
