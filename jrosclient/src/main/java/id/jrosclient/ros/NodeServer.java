package id.jrosclient.ros;

import java.util.Optional;

import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

import id.jrosclient.JRosClientConfig;
import id.xfunction.function.Unchecked;
import id.xfunction.logging.XLogger;

public class NodeServer implements AutoCloseable {

    static final XLogger LOGGER = XLogger.getLogger(NodeServer.class);
    private static final String CLASS_NAME = NodeServer.class.getName();

    private Optional<WebServer> server = Optional.empty();
    private int port = JRosClientConfig.NODE_SERVER_PORT;

    public NodeServer withPort(int port) {
        this.port = port;
        return this;
    }

    public void start() {
        if (!server.isEmpty()) return;
        var s = new WebServer(port);
        Unchecked.run(() -> startInternal(s));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                s.shutdown();
            }
        });
        server = Optional.of(s);
    }
    
    private void startInternal(WebServer s) throws Exception {
        LOGGER.fine("Starting...");
        XmlRpcServer xmlRpcServer = s.getXmlRpcServer();
        xmlRpcServer.setHandlerMapping(new MethodHandlerMapping(new NodeApiServerDispatcher()));
        XmlRpcServerConfigImpl serverConfig =
            (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
        serverConfig.setEnabledForExtensions(false);
        serverConfig.setContentLengthOptional(false);
        Unchecked.run(s::start);
    }

    public String getNodeApi() {
        return "http://localhost:" + port;
    }

    @Override
    public void close() {
        LOGGER.entering(CLASS_NAME, "close");
        LOGGER.fine("Stopping...");
        server.ifPresent(WebServer::shutdown);
        LOGGER.exiting(CLASS_NAME, "close");
    }
}
