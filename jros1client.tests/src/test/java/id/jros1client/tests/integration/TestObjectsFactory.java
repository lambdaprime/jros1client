/*
 * Copyright 2022 jrosclient project
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
package id.jros1client.tests.integration;

import id.jros1client.JRos1ClientConfiguration;
import id.jros1client.impl.ObjectsFactory;
import id.jros1client.ros.NodeServer;
import id.jros1client.ros.transport.PublishersManager;
import id.jros1client.ros.transport.TcpRosServer;
import id.jrosclient.utils.TextUtils;
import id.xfunction.logging.TracingToken;

public class TestObjectsFactory extends ObjectsFactory {

    NodeServer nodeServer;
    TcpRosServer tcpRosServer;

    @Override
    public NodeServer createNodeServer(TracingToken tracingToken, JRos1ClientConfiguration config) {
        if (nodeServer == null) nodeServer = super.createNodeServer(tracingToken, config);
        return nodeServer;
    }

    @Override
    public TcpRosServer createTcpRosServer(
            TracingToken tracingToken,
            PublishersManager publishersManager,
            JRos1ClientConfiguration config,
            TextUtils textUtils) {
        if (tcpRosServer == null)
            tcpRosServer =
                    super.createTcpRosServer(tracingToken, publishersManager, config, textUtils);
        return tcpRosServer;
    }
}
