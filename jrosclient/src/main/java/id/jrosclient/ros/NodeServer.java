/*
 * Copyright 2020 jrosclient project
 * 
 * Website: https://github.com/lambdaprime/jrosclient
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
package id.jrosclient.ros;

import id.jrosclient.JRosClientConfiguration;
import id.xfunction.function.Unchecked;
import id.xfunction.logging.XLogger;
import java.util.Optional;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

/**
 * XMLRPC server which is used to negotiate connections with other ROS nodes and communicate with
 * the Master.
 */
/** @author lambdaprime intid@protonmail.com */
public class NodeServer implements AutoCloseable {

    static final XLogger LOGGER = XLogger.getLogger(NodeServer.class);
    private static final String CLASS_NAME = NodeServer.class.getName();

    private Optional<WebServer> server = Optional.empty();
    private JRosClientConfiguration config;
    private boolean isClosed;

    public NodeServer(JRosClientConfiguration config) {
        this.config = config;
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
        LOGGER.fine("Starting...");
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
        LOGGER.entering(CLASS_NAME, "close");
        LOGGER.fine("Stopping...");
        server.ifPresent(WebServer::shutdown);
        isClosed = true;
        LOGGER.exiting(CLASS_NAME, "close");
    }

    public boolean isClosed() {
        return isClosed;
    }
}
