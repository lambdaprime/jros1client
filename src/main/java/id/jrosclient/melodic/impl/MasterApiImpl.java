package id.jrosclient.melodic.impl;

import id.jrosclient.XmlRpcClient;
import id.jrosclient.melodic.MasterApi;
import id.jrosclient.melodic.SystemState;
import id.jrosclient.melodic.impl.parsers.SystemStateParser;

public class MasterApiImpl implements MasterApi {

    private XmlRpcClient client;
    private SystemStateParser systemStateParser = new SystemStateParser();

    @Override
    public SystemState getSystemState(String callerId) {
        Object[] params = new Object[]{callerId};
        return systemStateParser.parse(client.execute("getSystemState", params));
    }

}
