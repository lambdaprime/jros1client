package id.jrosclient.melodic.impl;

import id.jrosclient.RosRpcClient;
import id.jrosclient.melodic.MasterApi;
import id.jrosclient.melodic.StringResponse;
import id.jrosclient.melodic.SystemStateResponse;
import id.jrosclient.melodic.impl.parsers.StringParser;
import id.jrosclient.melodic.impl.parsers.SystemStateParser;

public class MasterApiImpl implements MasterApi {

    private RosRpcClient client;
    private SystemStateParser systemStateParser = new SystemStateParser();
    private StringParser stringParser = new StringParser();

    public MasterApiImpl(RosRpcClient client) {
        this.client = client;
    }

    @Override
    public SystemStateResponse getSystemState(String callerId) {
        Object[] params = new Object[]{callerId};
        return systemStateParser.parse(client.execute("getSystemState", params));
    }

    @Override
    public StringResponse getUri(String callerId) {
        Object[] params = new Object[]{callerId};
        return stringParser.parse("masterURI", client.execute("getUri", params));
    }

    @Override
    public StringResponse lookupService(String callerId, String service) {
        Object[] params = new Object[]{callerId, service};
        return stringParser.parse("serviceUrl", client.execute("lookupService", params));
    }

}
