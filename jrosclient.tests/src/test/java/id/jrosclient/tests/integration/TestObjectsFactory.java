/*
 * Copyright 2022 jrosclient project
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
package id.jrosclient.tests.integration;

import id.jrosclient.JRosClientConfiguration;
import id.jrosclient.impl.ObjectsFactory;
import id.jrosclient.impl.TextUtils;
import id.jrosclient.ros.NodeServer;
import id.jrosclient.ros.transport.PublishersManager;
import id.jrosclient.ros.transport.TcpRosServer;

public class TestObjectsFactory extends ObjectsFactory {

    NodeServer nodeServer;
    TcpRosServer tcpRosServer;

    @Override
    public NodeServer createNodeServer(JRosClientConfiguration config) {
        if (nodeServer == null) nodeServer = super.createNodeServer(config);
        return nodeServer;
    }

    @Override
    public TcpRosServer createTcpRosServer(
            PublishersManager publishersManager,
            JRosClientConfiguration config,
            TextUtils textUtils) {
        if (tcpRosServer == null)
            tcpRosServer = super.createTcpRosServer(publishersManager, config, textUtils);
        return tcpRosServer;
    }
}
