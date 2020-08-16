package id.jrosclient.ros;

import java.util.Arrays;
import java.util.stream.Collectors;

import id.jrosclient.ros.api.NodeApi;
import id.jrosclient.ros.api.impl.NodeApiServerImpl;
import id.jrosclient.ros.entities.transformers.Transformers;
import id.jrosclient.ros.responses.parsers.ProtocolParamsParser;

/**
 * Dispatches the calls from Apache XML RPC client to NodeApi performing
 * required object transformations.
 */
public class NodeApiServerDispatcher {

    private ProtocolParamsParser protocolParamsParser = new ProtocolParamsParser();
    private Transformers transformers = new Transformers();
    private NodeApi nodeApi = new NodeApiServerImpl();

    public Object requestTopic(String callerId, String topic, Object[] protocols) {
        var protos = Arrays.stream(protocols)
            .map(transformers.protocolTransformer::transform)
            .collect(Collectors.toList());
        return protocolParamsParser.transform(nodeApi.requestTopic(callerId, topic, protos))
                .getObject();
    }

}
