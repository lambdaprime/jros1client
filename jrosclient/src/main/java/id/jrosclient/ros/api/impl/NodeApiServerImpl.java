package id.jrosclient.ros.api.impl;

import java.util.List;

import id.jrosclient.ros.api.NodeApi;
import id.jrosclient.ros.entities.Protocol;
import id.jrosclient.ros.responses.ProtocolParamsResponse;
import id.jrosclient.ros.responses.Response.StatusCode;
import id.xfunction.logging.XLogger;

/**
 * Server implementation of ROS Node API which allows to communicate
 * with remote ROS nodes.
 *
 */
public class NodeApiServerImpl implements NodeApi {

    private static final XLogger LOGGER = XLogger.getLogger(NodeApiServerImpl.class);

    @Override
    public ProtocolParamsResponse requestTopic(String callerId, String topic,
            List<Protocol> protocols) 
    {
        LOGGER.entering(LOGGER.getName(), "requestTopic", protocols);
        var response = new ProtocolParamsResponse();
        response.withStatusCode(StatusCode.SUCCESS);
        response.withStatusMessage("");
        response.withProtocol(Protocol.TCPROS);
        response.withPort(1235);
        response.withHost("ubuntu");
        LOGGER.exiting("requestTopic", response);
        return response;
    }

}
