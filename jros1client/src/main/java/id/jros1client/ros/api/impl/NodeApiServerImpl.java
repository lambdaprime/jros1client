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
package id.jros1client.ros.api.impl;

import id.jros1client.JRos1ClientConfiguration;
import id.jros1client.ros.api.NodeApi;
import id.jros1client.ros.entities.Protocol;
import id.jros1client.ros.responses.IntResponse;
import id.jros1client.ros.responses.ProtocolParamsResponse;
import id.jros1client.ros.responses.Response.StatusCode;
import id.xfunction.lang.XRE;
import id.xfunction.logging.XLogger;
import java.util.List;
import java.util.function.Predicate;

/**
 * Server implementation of ROS Node API which allows to communicate with remote ROS nodes.
 *
 * @author lambdaprime intid@protonmail.com
 */
public class NodeApiServerImpl implements NodeApi {

    private static final XLogger LOGGER = XLogger.getLogger(NodeApiServerImpl.class);

    private JRos1ClientConfiguration config;

    public NodeApiServerImpl(JRos1ClientConfiguration config) {
        this.config = config;
    }

    @Override
    public ProtocolParamsResponse requestTopic(
            String callerId, String topic, List<Protocol> protocols) {
        LOGGER.entering("requestTopic", protocols);
        boolean hasTcpRos =
                protocols.stream()
                        .map(Protocol::getProtocolName)
                        .anyMatch(Predicate.isEqual(Protocol.TCPROS.protocolName));
        if (!hasTcpRos) throw new XRE("Subscriber %s requested non TCPROS protocol", callerId);
        var response = new ProtocolParamsResponse();
        response.withStatusCode(StatusCode.SUCCESS);
        response.withStatusMessage("ready");
        response.withProtocol(Protocol.TCPROS);
        response.withPort(config.getTcpRosServerPort());
        response.withHost(config.getHostName());
        LOGGER.exiting("requestTopic", response);
        return response;
    }

    @Override
    public IntResponse publisherUpdate(String callerId, String topic, List<String> publishers) {
        LOGGER.warning("Operation publisherUpdate is not supported, ignoring...");
        var response = new IntResponse("ignore");
        response.withStatusCode(StatusCode.SUCCESS);
        response.withStatusMessage("ready");
        return response;
    }
}
