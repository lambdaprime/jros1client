package id.jrosclient.melodic.impl.parsers;

import id.jrosclient.RawResponse;
import id.jrosclient.melodic.SystemState;

public class SystemStateParser extends ResponseParser {

    private PublisherParser pr = new PublisherParser();

    public SystemState parse(RawResponse response) {
        SystemState ss = new SystemState();
        populate(ss, response);
        RawResponse systemState = response.get(2);
        for (RawResponse publisher: systemState.get(0)) {
            ss.publishers.add(pr.parse(publisher));
        }
        return ss;
    }
    
}
