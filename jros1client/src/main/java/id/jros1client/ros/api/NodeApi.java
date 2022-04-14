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
package id.jros1client.ros.api;

import id.jros1client.ros.entities.Protocol;
import id.jros1client.ros.responses.IntResponse;
import id.jros1client.ros.responses.ProtocolParamsResponse;
import java.util.List;

/**
 * An API to interact with ROS nodes.
 *
 * @see <a href="http://wiki.ros.org/ROS/Slave_API">ROS Node API</a>
 */
/** @author lambdaprime intid@protonmail.com */
public interface NodeApi {

    /**
     * Publisher node API method called by a subscriber node. It requests publisher node to allocate
     * a channel for communication. Subscriber provides a list of desired protocols for
     * communication. Publisher returns the selected protocol along with any additional parameters
     * required for establishing connection. For example, for a TCP/IP-based connection, the
     * publisher node may return a port number of its TCP/IP server.
     *
     * @param callerId ROS caller ID
     * @param topic Topic name
     * @param protocols List of desired protocols for communication in order of preference.
     */
    ProtocolParamsResponse requestTopic(String callerId, String topic, List<Protocol> protocols);

    /**
     * Callback from master of current publisher list for specified topic.
     *
     * @param callerId ROS caller ID
     * @param topic Topic name
     */
    IntResponse publisherUpdate(String callerId, String topic, List<String> publishers);
}
