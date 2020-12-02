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
/*
 * Authors:
 * - lambdaprime <id.blackmesa@gmail.com>
 */
package id.jrosclient;

/**
 * Configuration parameters
 */
public class JRosClientConfiguration {

    public static final int DEFAULT_TCP_ROS_SERVER_PORT = 1235;
    public static final int DEFAULT_NODE_SERVER_PORT = 1234;
    public static final String HOST_NAME = "localhost";

    private int tcpRosServerPort = DEFAULT_TCP_ROS_SERVER_PORT;
    private int nodeServerPort = DEFAULT_NODE_SERVER_PORT;
    private String hostName = HOST_NAME;

    /**
     * <p>Port for TCPROS.</p>
     * <p>TCPROS is a transport layer responsible for publishing messages.</p>
     * <p>This is a port to which other ROS nodes connect
     * once they subscribe to any topic published through JRosClient.</p>
     * <p>Default value is {@link DEFAULT_TCP_ROS_SERVER_PORT}</p>
     */
    public int getTcpRosServerPort() {
        return tcpRosServerPort;
    }

    public void setTcpRosServerPort(int tcpRosServerPort) {
        this.tcpRosServerPort = tcpRosServerPort;
    }

    /**
     * <p>Port for running Node server (XMLRPC server).</p>
     * <p>This server is used to negotiate connections with other ROS nodes and
     * communicate with the Master.</p> 
     * <p>Default value is {@link DEFAULT_NODE_SERVER_PORT}</p>
     */
    public int getNodeServerPort() {
        return nodeServerPort;
    }

    public void setNodeServerPort(int nodeServerPort) {
        this.nodeServerPort = nodeServerPort;
    }

    /**
     * Name of the host where TCPROS server and Node server will be running on.
     * This host name should belong to the host where jrosclient is used and to
     * which other ROS nodes can communicate.
     * <p>Default value is {@link HOST_NAME}</p>
     */
    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
    
}
