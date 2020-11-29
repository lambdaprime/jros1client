package id.jrosclient;

/**
 * Configuration parameters
 */
public interface JRosClientConfig {

    /**
     * <p>Port for TCPROS.</p>
     * <p>TCPROS is a transport layer responsible for publishing messages.</p>
     * <p>This is a port to which other ROS nodes connect
     * once they subscribe to any topic published through JRosClient.</p>
     */
    int TCPROS_SERVER_PORT = 1235;
    
    /**
     * <p>Port for running Node server (XMLRPC server).</p>
     * <p>This server is used to negotiate connections with other ROS nodes and
     * communicate with the Master.</p> 
     */
    int NODE_SERVER_PORT = 1234;

}
