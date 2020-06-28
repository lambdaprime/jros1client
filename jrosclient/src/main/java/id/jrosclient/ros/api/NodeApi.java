package id.jrosclient.ros.api;

import java.util.List;

import id.jrosclient.ros.entities.Protocol;
import id.jrosclient.ros.responses.ProtocolParamsResponse;

/**
 * @see <a href="http://wiki.ros.org/ROS/Slave_API">ROS Node API</a>
 */
public interface NodeApi {

    /**
     * Publisher node API method called by a subscriber node. This requests
     * that source allocate a channel for communication. Subscriber provides
     * a list of desired protocols for communication. Publisher returns the
     * selected protocol along with any additional params required for establishing
     * connection. For example, for a TCP/IP-based connection, the source node may
     * return a port number of TCP/IP server. 
     * 
     * @param callerId ROS caller ID
     * @param topic Topic name
     * @param protocols List of desired protocols for communication in order of preference.
     * @return
     */
    ProtocolParamsResponse requestTopic(String callerId, String topic, List<Protocol> protocols);
}
