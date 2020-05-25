package id.jrosclient.app;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import id.jrosclient.JRosClient;
import id.jrosclient.ros.NodeClient;
import id.jrosclient.ros.api.impl.NodeApiServer;
import id.jrosclient.ros.entities.Protocol;
import id.jrosclient.ros.transport.ConnectionHeader;
import id.jrosclient.ros.transport.MessagePacket;
import id.jrosmessages.MessageTransformer;
import id.jrosmessages.MessagesDirectory;
import id.xfunction.ArgumentParsingException;
import id.xfunction.XUtils;
import id.xfunction.function.Unchecked;
import id.xfunction.logging.XLogger;

public class RosTopic {

    private static final Logger LOGGER = XLogger.getLogger(NodeApiServer.class);
    private static final String CALLER_ID = "jrosclient";
    private String masterUrl;
    private int nodePort;
    private MessagesDirectory messagesDirectory = new MessagesDirectory();

    public RosTopic(String masterUrl, int nodePort) {
        this.masterUrl = masterUrl;
        this.nodePort = nodePort;
    }

    public void execute(List<String> positionalArgs) {
        Unchecked.run(() -> executeInternal(positionalArgs));
    }

    public void executeInternal(List<String> args) throws ArgumentParsingException {
        var rest = new LinkedList<>(args);
        var cmd = rest.removeFirst();
        switch (cmd) {
        case "echo" : Unchecked.run(() -> echo(rest));
        break;
        default: throw new ArgumentParsingException();
        }
    }

    private void echo(LinkedList<String> rest) throws Exception {
        JRosClient client = new JRosClient(masterUrl, nodePort);
        var topic = rest.removeFirst();
        var topicType = rest.removeFirst();
        Class<?> clazz = messagesDirectory.get(topicType);
        if (clazz == null)
            XUtils.throwRuntime("Type %s is not found", topicType);
        var publishers = client.getMasterApi().registerSubscriber(CALLER_ID, topic, topicType);
        LOGGER.log(Level.FINE, "Publishers: {0}", publishers.toString());
        if (publishers.value.isEmpty()) {
            throw new RuntimeException("No publishers for topic " + topic + " found");
        }
        var nodeApi = client.getNodeApi(publishers.value.get(0));
        var protocol = nodeApi.requestTopic(CALLER_ID, topic, List.of(Protocol.TCPROS));
        LOGGER.log(Level.FINE, "Protocol configuration: {0}", protocol);
        var nodeClient = NodeClient.connect(protocol.host, protocol.port);
        Consumer<MessagePacket> handler = response -> {
            LOGGER.log(Level.FINE, "Message packet: {0}", response);
            var msg = new MessageTransformer().transform(response.getBody(), clazz);
            System.out.println(msg);
            Unchecked.run(nodeClient::close);
        };
        nodeClient.setHandler(handler);
        String messageDefinition = "string data";
        var ch = new ConnectionHeader()
                .withTopic("/" + topic)
                .withCallerId(CALLER_ID)
                .withType(topicType)
                .withMessageDefinition(messageDefinition )
                .withMd5Sum(messagesDirectory.getMd5(clazz));
        nodeClient.start(ch);
        client.close();
    }

}
