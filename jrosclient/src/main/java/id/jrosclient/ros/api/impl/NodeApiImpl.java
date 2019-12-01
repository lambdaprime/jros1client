package id.jrosclient.ros.api.impl;

import java.util.List;

import id.jrosclient.impl.RosRpcClient;
import id.jrosclient.ros.api.NodeApi;
import id.jrosclient.ros.entities.Protocol;
import id.jrosclient.ros.entities.transformers.Transformers;
import id.jrosclient.ros.responses.ProtocolParamsResponse;
import id.jrosclient.ros.responses.parsers.ProtocolParamsParser;

public class NodeApiImpl implements NodeApi {

    private RosRpcClient client;
    private ProtocolParamsParser protocolParamsParser = new ProtocolParamsParser();
    private Transformers transformers = new Transformers();

    public NodeApiImpl(RosRpcClient client) {
        this.client = client;
    }

    @Override
    public ProtocolParamsResponse requestTopic(String callerId, String topic, List<Protocol> protocols) {
        var array = protocols.stream()
            .map(transformers.protocolTransformer::transform)
            .toArray();
        Object[] params = new Object[]{callerId, topic, array};
        return protocolParamsParser.parse(
                client.execute("requestTopic", params));
    }

}
