package id.jrosclient.melodic.impl;

import id.jrosclient.RosRpcClient;
import id.jrosclient.melodic.MasterApi;
import id.jrosclient.melodic.SystemState;
import id.jrosclient.melodic.impl.parsers.SystemStateParser;

public class MasterApiImpl implements MasterApi {

    private RosRpcClient client;
    private SystemStateParser systemStateParser = new SystemStateParser();

    public MasterApiImpl(RosRpcClient client) {
        this.client = client;
    }

    @Override
    public SystemState getSystemState(String callerId) {
        Object[] params = new Object[]{callerId};
        return systemStateParser.parse(client.execute("getSystemState", params));
    }

}
