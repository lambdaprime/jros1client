package id.jrosclient.ros.api.impl;

import java.util.List;
import java.util.function.Predicate;

import id.jrosclient.JRosClientConfiguration;
import id.jrosclient.ros.api.NodeApi;
import id.jrosclient.ros.entities.Protocol;
import id.jrosclient.ros.responses.ProtocolParamsResponse;
import id.jrosclient.ros.responses.Response.StatusCode;
import id.xfunction.XRE;
import id.xfunction.logging.XLogger;

/**
 * Server implementation of ROS Node API which allows to communicate
 * with remote ROS nodes.
 *
 */
public class NodeApiServerImpl implements NodeApi {

    private static final XLogger LOGGER = XLogger.getLogger(NodeApiServerImpl.class);
    
    private JRosClientConfiguration config;

    public NodeApiServerImpl(JRosClientConfiguration config) {
        this.config = config;
    }

    @Override
    public ProtocolParamsResponse requestTopic(String callerId, String topic,
            List<Protocol> protocols) 
    {
        LOGGER.entering(LOGGER.getName(), "requestTopic", protocols);
        boolean hasTcpRos = protocols.stream()
                .map(Protocol::getProtocolName)
                .anyMatch(Predicate.isEqual(Protocol.TCPROS.protocolName));
        if (!hasTcpRos) throw new XRE("Subscriber %s requested non TCPROS protocol",
                callerId);
        var response = new ProtocolParamsResponse();
        response.withStatusCode(StatusCode.SUCCESS);
        response.withStatusMessage("ready on ubuntu:38245");
        response.withProtocol(Protocol.TCPROS);
        response.withPort(config.getTcpRosServerPort());
        response.withHost(config.getHostName());
        LOGGER.exiting("requestTopic", response);
        return response;
    }

}
