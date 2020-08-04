package id.jrosclient.ros.api.impl;

import java.util.List;
import java.util.logging.Logger;

import id.jrosclient.ros.api.NodeApi;
import id.jrosclient.ros.entities.Protocol;
import id.jrosclient.ros.responses.ProtocolParamsResponse;
import id.xfunction.logging.XLogger;

/**
 * Server implementation of ROS Node API which allows to communicate
 * with remote ROS nodes.
 *
 */
public class NodeApiServerImpl implements NodeApi {

    private static final Logger LOGGER = XLogger.getLogger(NodeApiServerImpl.class);

    @Override
    public ProtocolParamsResponse requestTopic(String callerId, String topic,
            List<Protocol> protocols) 
    {
        LOGGER.entering(LOGGER.getName(), "requestTopic", protocols);
        var response = new ProtocolParamsResponse();
        response.withProtocol(Protocol.TCPROS);
        response.withPort(1234);
        response.withHost("localhost");
        LOGGER.exiting(LOGGER.getName(), "requestTopic", response);
        return response;
    }

}
