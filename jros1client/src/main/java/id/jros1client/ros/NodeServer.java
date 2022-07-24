/*
 * Copyright 2020 jrosclient project
 * 
 * Website: https://github.com/lambdaprime/jros1client
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package id.jros1client.ros;

import id.jros1client.JRos1ClientConfiguration;
import id.xfunction.function.Unchecked;
import id.xfunction.logging.TracingToken;
import id.xfunction.logging.XLogger;
import java.util.Optional;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

/**
 * XMLRPC server which is used to negotiate connections with other ROS nodes and communicate with
 * the Master.
 */
/**
 * @author lambdaprime intid@protonmail.com
 */
public class NodeServer implements AutoCloseable {

    static XLogger logger = XLogger.getLogger(NodeServer.class);
    private static final String CLASS_NAME = NodeServer.class.getName();

    private Optional<WebServer> server = Optional.empty();
    private JRos1ClientConfiguration config;
    private boolean isClosed;

    public NodeServer(
            @SuppressWarnings("exports") TracingToken tracingToken,
            JRos1ClientConfiguration config) {
        this.config = config;
        logger = XLogger.getLogger(NodeServer.class, tracingToken);
    }

    public void start() {
        if (!server.isEmpty()) return;
        var s = new WebServer(config.getNodeServerPort());
        Unchecked.run(() -> startInternal(s));
        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread() {
                            @Override
                            public void run() {
                                s.shutdown();
                            }
                        });
        server = Optional.of(s);
    }

    private void startInternal(WebServer s) throws Exception {
        logger.fine("Starting...");
        XmlRpcServer xmlRpcServer = s.getXmlRpcServer();
        xmlRpcServer.setHandlerMapping(
                new MethodHandlerMapping(new NodeApiServerDispatcher(config)));
        XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
        serverConfig.setEnabledForExtensions(false);
        serverConfig.setContentLengthOptional(false);
        Unchecked.run(s::start);
    }

    @Override
    public void close() {
        logger.entering(CLASS_NAME, "close");
        logger.fine("Stopping...");
        server.ifPresent(WebServer::shutdown);
        isClosed = true;
        logger.exiting(CLASS_NAME, "close");
    }

    public boolean isClosed() {
        return isClosed;
    }
}
