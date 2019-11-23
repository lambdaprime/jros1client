package id.jrosclient.ros.responses.parsers;

import id.jrosclient.ros.impl.RawResponse;
import id.jrosclient.ros.responses.SystemStateResponse;

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
