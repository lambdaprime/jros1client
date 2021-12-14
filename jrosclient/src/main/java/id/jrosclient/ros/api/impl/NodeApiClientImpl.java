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
 * - lambdaprime <intid@protonmail.com>
 */
package id.jrosclient.ros.api.impl;

import java.util.List;
import java.util.logging.Logger;

import id.jrosclient.impl.RosRpcClient;
import id.jrosclient.ros.api.NodeApi;
import id.jrosclient.ros.entities.Protocol;
import id.jrosclient.ros.entities.transformers.Transformers;
import id.jrosclient.ros.responses.IntResponse;
import id.jrosclient.ros.responses.ProtocolParamsResponse;
import id.jrosclient.ros.responses.transformers.ProtocolParamsTransformer;
import id.xfunction.logging.XLogger;

/**
 * Client implementation of ROS Node API which allows to communicate
 * with remote ROS nodes.
 *
 */
public class NodeApiClientImpl implements NodeApi {

    private static final Logger LOGGER = XLogger.getLogger(NodeApiClientImpl.class);
    private RosRpcClient client;
    private ProtocolParamsTransformer protocolParamsParser = new ProtocolParamsTransformer();
    private Transformers transformers = new Transformers();

    public NodeApiClientImpl(RosRpcClient client) {
        this.client = client;
    }

    @Override
    public ProtocolParamsResponse requestTopic(String callerId, String topic, List<Protocol> protocols) {
        var array = protocols.stream()
            .map(transformers.protocolTransformer::transform)
            .toArray();
        Object[] params = new Object[]{callerId, topic, array};
        return protocolParamsParser.parse(
                client.execute("requestTopic", params));
    }

    @Override
    public IntResponse publisherUpdate(String callerId, String topic, List<String> publishers) {
        LOGGER.severe("Operation publisherUpdate is not supported, ignoring...");
        return new IntResponse("ignore");
    }

}
