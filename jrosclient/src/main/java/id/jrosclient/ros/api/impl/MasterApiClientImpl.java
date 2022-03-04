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
package id.jrosclient.ros.api.impl;

import id.jrosclient.impl.RosRpcClient;
import id.jrosclient.ros.api.MasterApi;
import id.jrosclient.ros.entities.transformers.Transformers;
import id.jrosclient.ros.responses.IntResponse;
import id.jrosclient.ros.responses.ListResponse;
import id.jrosclient.ros.responses.StringResponse;
import id.jrosclient.ros.responses.SystemStateResponse;
import id.jrosclient.ros.responses.transformers.IntTransformer;
import id.jrosclient.ros.responses.transformers.ListTransformer;
import id.jrosclient.ros.responses.transformers.StringTransformer;
import id.jrosclient.ros.responses.transformers.SystemStateTransformer;

/** @author lambdaprime intid@protonmail.com */
public class MasterApiClientImpl implements MasterApi {

    private RosRpcClient client;
    private SystemStateTransformer systemStateParser;
    private StringTransformer stringParser = new StringTransformer();
    private ListTransformer stringListParser = new ListTransformer();
    private IntTransformer intParser = new IntTransformer();

    public MasterApiClientImpl(RosRpcClient client) {
        this.client = client;
        var transformers = new Transformers();
        this.systemStateParser =
                new SystemStateTransformer(
                        transformers.publisherTransformer, transformers.subscriberTransformer);
    }

    @Override
    public SystemStateResponse getSystemState(String callerId) {
        Object[] params = new Object[] {callerId};
        return systemStateParser.parse(client.execute("getSystemState", params));
    }

    @Override
    public StringResponse getUri(String callerId) {
        Object[] params = new Object[] {callerId};
        return stringParser.parse("masterURI", client.execute("getUri", params));
    }

    // @Override
    public StringResponse lookupService(String callerId, String service) {
        Object[] params = new Object[] {callerId, service};
        return stringParser.parse("serviceUrl", client.execute("lookupService", params));
    }

    @Override
    public ListResponse<String> registerPublisher(
            String callerId, String topic, String topicType, String callerApi) {
        Object[] params = new Object[] {callerId, topic, topicType, callerApi};
        return stringListParser.parseString(
                "subscriberApis", client.execute("registerPublisher", params));
    }

    @Override
    public ListResponse<String> registerSubscriber(
            String callerId, String topic, String topicType, String callerApi) {
        Object[] params = new Object[] {callerId, topic, topicType, callerApi};
        return stringListParser.parseString(
                "publishers", client.execute("registerSubscriber", params));
    }

    @Override
    public IntResponse unregisterPublisher(String callerId, String topic, String callerApi) {
        Object[] params = new Object[] {callerId, topic, callerApi};
        return intParser.parse("subscriberApis", client.execute("unregisterPublisher", params));
    }
}
