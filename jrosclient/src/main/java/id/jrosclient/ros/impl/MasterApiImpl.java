package id.jrosclient.ros.impl;

import id.jrosclient.impl.RosRpcClient;
import id.jrosclient.ros.MasterApi;
import id.jrosclient.ros.entities.transformers.Transformers;
import id.jrosclient.ros.responses.ListResponse;
import id.jrosclient.ros.responses.StringResponse;
import id.jrosclient.ros.responses.SystemStateResponse;
import id.jrosclient.ros.responses.parsers.ListParser;
import id.jrosclient.ros.responses.parsers.StringParser;
import id.jrosclient.ros.responses.parsers.SystemStateParser;

public class MasterApiImpl implements MasterApi {

    private RosRpcClient client;
    private NodeServer nodeServer;
    private SystemStateParser systemStateParser;
    private StringParser stringParser = new StringParser();
    private ListParser stringListParser = new ListParser();

    public MasterApiImpl(RosRpcClient client, NodeServer nodeServer) {
        this.client = client;
        this.nodeServer = nodeServer;
        var transformers = new Transformers();
        this.systemStateParser = new SystemStateParser(transformers.publisherTransformer);
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

    @Override
    public ListResponse<String> registerPublisher(String callerId, String topic,
            String topicType) 
    {
        nodeServer.start();
        Object[] params = new Object[]{callerId, topic, topicType, nodeServer.getNodeApi()};
        return stringListParser.parseString("subscriberApis", client.execute("registerPublisher", params));
    }

}
