package id.jrosclient.ros;

import java.util.Arrays;
import java.util.stream.Collectors;

import id.jrosclient.JRosClientConfiguration;
import id.jrosclient.ros.api.NodeApi;
import id.jrosclient.ros.api.impl.NodeApiServerImpl;
import id.jrosclient.ros.entities.transformers.Transformers;
import id.jrosclient.ros.responses.transformers.ProtocolParamsTransformer;

/**
 * Dispatches the calls from Apache XML RPC client to NodeApi performing
 * required object transformations.
 */
public class NodeApiServerDispatcher {

    private ProtocolParamsTransformer protocolParamsParser = new ProtocolParamsTransformer();
    private Transformers transformers = new Transformers();
    private NodeApi nodeApi;

    public NodeApiServerDispatcher(JRosClientConfiguration config) {
        nodeApi = new NodeApiServerImpl(config);
    }

    public Object requestTopic(String callerId, String topic, Object[] protocols) {
        var protos = Arrays.stream(protocols)
            .map(transformers.protocolTransformer::transform)
            .collect(Collectors.toList());
        return protocolParamsParser.transform(nodeApi.requestTopic(callerId, topic, protos))
                .getObject();
    }

}
