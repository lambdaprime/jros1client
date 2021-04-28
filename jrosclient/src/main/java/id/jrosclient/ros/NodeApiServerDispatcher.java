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
package id.jrosclient.ros;

import java.util.Arrays;
import java.util.stream.Collectors;

import id.jrosclient.JRosClientConfiguration;
import id.jrosclient.ros.api.NodeApi;
import id.jrosclient.ros.api.impl.NodeApiServerImpl;
import id.jrosclient.ros.entities.transformers.Transformers;
import id.jrosclient.ros.responses.transformers.ProtocolParamsTransformer;

/**
 * Dispatches the calls from Apache XML RPC client to NodeApi performing
 * required object transformations.
 */
public class NodeApiServerDispatcher {

    private ProtocolParamsTransformer protocolParamsParser = new ProtocolParamsTransformer();
    private Transformers transformers = new Transformers();
    private NodeApi nodeApi;

    public NodeApiServerDispatcher(JRosClientConfiguration config) {
        nodeApi = new NodeApiServerImpl(config);
    }

    public Object requestTopic(String callerId, String topic, Object[] protocols) {
        var protos = Arrays.stream(protocols)
            .map(transformers.protocolTransformer::transform)
            .collect(Collectors.toList());
        return protocolParamsParser.transform(nodeApi.requestTopic(callerId, topic, protos))
                .getObject();
    }

}
