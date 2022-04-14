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

import id.jros1client.ros.responses.IntResponse;
import id.jros1client.ros.responses.ListResponse;
import id.jros1client.ros.responses.StringResponse;
import id.jros1client.ros.responses.SystemStateResponse;

/**
 * An API to interact with ROS Master node.
 *
 * @see <a href="http://wiki.ros.org/ROS/Master_API">ROS Maste API</a>
 * @author lambdaprime intid@protonmail.com
 */
public interface MasterApi {

    /**
     * Retrieve list representation of system state (for example available publishers, subscribers
     * and services).
     *
     * @param callerId ROS caller ID
     */
    SystemStateResponse getSystemState(String callerId);

    /**
     * Get the URI of the master
     *
     * @param callerId ROS caller ID
     * @return masterURI
     */
    StringResponse getUri(String callerId);

    /**
     * Register the caller as a publisher of the topic.
     *
     * @param callerId ROS caller ID
     * @param topic Fully-qualified name of the topic
     * @param topicType Type of the messages in the given topic
     * @param callerApi URI of publisher API to register (other nodes will use that API to subscribe
     *     for the topic)
     * @return List of current subscribers of the topic
     */
    ListResponse<String> registerPublisher(
            String callerId, String topic, String topicType, String callerApi);

    /**
     * Unregister the caller from being publisher of the topic. Unregistration will only occur if
     * current registration matches.
     *
     * @param callerId ROS caller ID
     * @param topic Fully-qualified name of the topic to unregister.
     * @param callerApi URI of publisher API to unregister.
     * @return number of unregistered publishers. If it is zero it means that the caller was not
     *     registered as a publisher. The call still succeeds as the intended final state is
     *     reached.
     */
    IntResponse unregisterPublisher(String callerId, String topic, String callerApi);

    /**
     * Subscribe the caller to the specified topic
     *
     * @param callerId ROS caller ID
     * @param topic Fully-qualified name of the topic
     * @param topicType Type of the messages in the given topic
     * @param callerApi URI of subscriber API to register. If topic is not yet available it will be
     *     used later to notify new publisher about already active subscribers.
     * @return list of current publishers
     */
    ListResponse<String> registerSubscriber(
            String callerId, String topic, String topicType, String callerApi);

    // StringResponse lookupService(String callerId, String service);

}
