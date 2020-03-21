package id.jrosclient.app;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import id.jrosclient.JRosClient;
import id.jrosclient.ros.api.impl.NodeApiServer;
import id.jrosclient.ros.entities.Protocol;
import id.xfunction.XLogger;
import id.xfunction.XUtils;
import id.xfunction.function.Unchecked;

public class RosTopic {

    private static final Logger LOGGER = XLogger.getLogger(NodeApiServer.class);
    private static final String CALLER_ID = "jrosclient";
    private String masterUrl;
    private int nodePort;

    public RosTopic(String masterUrl, int nodePort) {
        this.masterUrl = masterUrl;
        this.nodePort = nodePort;
    }

    public void execute(List<String> positionalArgs) {
        Unchecked.runUnchecked(() -> executeInternal(positionalArgs));
    }

    public void executeInternal(List<String> args) throws Exception {
        var rest = new LinkedList<>(args);
        var cmd = rest.removeFirst();
        switch (cmd) {
        case "echo" : echo(rest);
        break;
        default: XUtils.error("Command unknown");
        }
    }

    private void echo(LinkedList<String> rest) throws Exception {
        JRosClient client = new JRosClient(masterUrl, nodePort);
        var topic = rest.removeFirst();
        var topicType = rest.removeFirst();
        var publishers = client.getMasterApi().registerSubscriber(CALLER_ID, topic, topicType);
        LOGGER.info(publishers.toString());
        if (publishers.value.isEmpty()) {
            throw new RuntimeException("No publishers for topic " + topic + " found");
        }
        var nodeApi = client.getNodeApi(publishers.value.get(0));
        var protocols = nodeApi.requestTopic(CALLER_ID, topic, List.of(Protocol.TCPROS));
        LOGGER.info(protocols.toString());
        client.close();
    }

}
