package id.jrosclient.melodic.impl.parsers;

import id.jrosclient.melodic.SystemState;

public class SystemStateParser extends ResponseParser {

    public SystemState parse(Object[] response) {
        SystemState ss = new SystemState();
        populate(ss, response);
        return ss;
    }
}
