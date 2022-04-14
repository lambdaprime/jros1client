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
package id.jros1client.ros;

import id.jros1client.JRos1ClientConfiguration;
import id.jros1client.ros.api.NodeApi;
import id.jros1client.ros.api.impl.NodeApiServerImpl;
import id.jros1client.ros.entities.transformers.Transformers;
import id.jros1client.ros.responses.transformers.IntTransformer;
import id.jros1client.ros.responses.transformers.ProtocolParamsTransformer;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Dispatches the calls from Apache XML RPC client to NodeApi performing required object
 * transformations.
 */
/** @author lambdaprime intid@protonmail.com */
public class NodeApiServerDispatcher {

    private ProtocolParamsTransformer protocolParamsParser = new ProtocolParamsTransformer();
    private IntTransformer intTransformer = new IntTransformer();
    private Transformers transformers = new Transformers();
    private NodeApi nodeApi;

    public NodeApiServerDispatcher(JRos1ClientConfiguration config) {
        nodeApi = new NodeApiServerImpl(config);
    }

    public Object requestTopic(String callerId, String topic, Object[] protocols) {
        var protos =
                Arrays.stream(protocols)
                        .map(transformers.protocolTransformer::transform)
                        .collect(Collectors.toList());
        return protocolParamsParser
                .transform(nodeApi.requestTopic(callerId, topic, protos))
                .getObject();
    }

    public Object publisherUpdate(String callerId, String topic, Object[] publishers) {
        var pubs = Arrays.stream(publishers).map(Object::toString).collect(Collectors.toList());
        return intTransformer.transform(nodeApi.publisherUpdate(callerId, topic, pubs)).getObject();
    }
}
