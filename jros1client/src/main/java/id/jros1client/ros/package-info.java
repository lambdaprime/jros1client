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
/**
 * Implementation of ROS1 transport.
 *
 * <h2>ROS1 transport overview</h2>
 *
 * <p>ROS1 transport protocol consist from 3 parts:
 *
 * <ul>
 *   <li><a href="http://wiki.ros.org/ROS/Master_API">ROS Maste API</a> - An API to interact with
 *       ROS1 Master node
 *   <li><a href="http://wiki.ros.org/ROS/Slave_API">ROS Node API</a> - An API which is used by
 *       Master node and nodes themselves to communicate with each other
 *   <li><a href="http://wiki.ros.org/ROS/TCPROS">TCPROS</a> - protocol used to transfer data
 *       (messages) between the Nodes
 * </ul>
 *
 * <img alt="" src="doc-files/overview-rostransport.png"/>
 *
 * <h2>jros1client transport implementation</h2>
 *
 * <p>To implement ROS1 transport <b>jros1client</b> is relying on classes which hierarchy looks as
 * follows:
 *
 * <p><img alt="" src="doc-files/hierarchy.png"/>
 *
 * <p>Not all of these classes are exported from <b>jros1client</b> module to the users because they
 * are part of <b>jros1client</b> implementation.
 *
 * @author lambdaprime intid@protonmail.com
 */
package id.jros1client.ros;
