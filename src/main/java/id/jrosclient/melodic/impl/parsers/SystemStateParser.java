package id.jrosclient.melodic.impl.parsers;

import id.jrosclient.RawResponse;
import id.jrosclient.melodic.responses.SystemStateResponse;

public class SystemStateParser extends ResponseParser {

    private PublisherParser pr = new PublisherParser();

    public SystemStateResponse parse(RawResponse response) {
        SystemStateResponse ss = new SystemStateResponse();
        populate(ss, response);
        RawResponse systemState = response.get(2);
        for (RawResponse publisher: systemState.get(0)) {
            ss.publishers.add(pr.parse(publisher));
        }
        return ss;
    }
    
}
