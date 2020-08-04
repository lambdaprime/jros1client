package id.jrosclient.app;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import id.jrosclient.JRosClient;
import id.jrosclient.TopicSubscriber;
import id.jrosclient.ros.responses.Response.StatusCode;
import id.jrosmessages.Message;
import id.jrosmessages.MessagesDirectory;
import id.xfunction.ArgumentParsingException;
import id.xfunction.XRE;
import id.xfunction.function.Unchecked;
import id.xfunction.logging.XLogger;

public class RosTopic {

    private static final Logger LOGGER = XLogger.getLogger(RosTopic.class);
    private static final String CALLER_ID = "jrosclient-rostopic";
    private String masterUrl;
    private Optional<Integer> nodePort;
    
    public RosTopic(String masterUrl, Optional<Integer> nodePort) {
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
        case "echo":
            Unchecked.run(() -> echo(rest));
            break;
        case "list":
            Unchecked.run(() -> list());
            break;
        default: throw new ArgumentParsingException();
        }
    }

    private void list() throws Exception {
        try (JRosClient client = new JRosClient(masterUrl)) {
            var systemState = client.getMasterApi().getSystemState(CALLER_ID);
            if (systemState.statusCode != StatusCode.SUCCESS) {
                throw new XRE("Failed to get system status: %s", systemState.statusMessage);
            }
            System.out.println(systemState);
        }
    }

    private void echo(LinkedList<String> rest) throws Exception {
        JRosClient client = new JRosClient(masterUrl);
            if (nodePort.isPresent())
                client.withPort(nodePort.get());
            var topic = rest.removeFirst();
            var topicType = rest.removeFirst();
            Class<Message> clazz = (Class<Message>) new MessagesDirectory().get(topicType);
            if (clazz == null)
                throw new XRE("Type %s is not found", topicType);
            var subscriber = new TopicSubscriber<Message>(clazz, topic) {
                @Override
                public void onNext(Message message) {
                    System.out.println(message);
                    getSubscription().cancel();
                    Unchecked.run(() -> client.close());
                }
            };
            client.subscribe(subscriber);
    }
    
}
