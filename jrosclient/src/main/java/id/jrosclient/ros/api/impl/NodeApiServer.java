package id.jrosclient.ros.api.impl;

import java.util.Optional;
import java.util.logging.Logger;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

import id.xfunction.XLogger;
import id.xfunction.function.Unchecked;

public class NodeApiServer implements AutoCloseable {

    private static final String CLASS_NAME = NodeApiServer.class.getName();
    private static final Logger LOGGER = XLogger.getLogger(NodeApiServer.class);

    private Optional<WebServer> server = Optional.empty();
    private int port;

    public NodeApiServer(int port) {
        this.port = port;
    }

    public void start() {
        if (!server.isEmpty()) return;
        var s = new WebServer(port);
        startInternal(s);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                s.shutdown();
            }
        });
        server = Optional.of(s);
    }
    
    private void startInternal(WebServer s) {
        XmlRpcServer xmlRpcServer = s.getXmlRpcServer();
        
        PropertyHandlerMapping phm = new PropertyHandlerMapping();
        //phm.addHandler("Calculator", asds.class);
        xmlRpcServer.setHandlerMapping(phm);
      
        XmlRpcServerConfigImpl serverConfig =
            (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
        serverConfig.setEnabledForExtensions(true);
        serverConfig.setContentLengthOptional(false);

        Unchecked.runUnchecked(s::start);
    }

    public String getNodeApi() {
        return "http://localhost:" + port;
    }

    @Override
    public void close() throws Exception {
        LOGGER.entering(CLASS_NAME, "close");
        server.ifPresent(WebServer::shutdown);
        LOGGER.exiting(CLASS_NAME, "close");
    }
}
