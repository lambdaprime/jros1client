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
package id.jros1client.impl;

import id.jros1client.JRos1ClientConfiguration;
import id.jros1client.ros.NodeServer;
import id.jros1client.ros.transport.PublishersManager;
import id.jros1client.ros.transport.TcpRosServer;
import id.jrosclient.utils.TextUtils;

/** This factory is a single point for managing all dependencies. */
public class ObjectsFactory {

    private static ObjectsFactory instance = new ObjectsFactory();

    public JRos1ClientConfiguration createConfig() {
        return new JRos1ClientConfiguration();
    }

    public NodeServer createNodeServer(JRos1ClientConfiguration config) {
        return new NodeServer(config);
    }

    public static ObjectsFactory getInstance() {
        return instance;
    }

    public static void setInstance(ObjectsFactory objectsFactory) {
        instance = objectsFactory;
    }

    public TcpRosServer createTcpRosServer(
            PublishersManager publishersManager,
            JRos1ClientConfiguration config,
            TextUtils textUtils) {
        return new TcpRosServer(publishersManager, config, textUtils);
    }

    public TextUtils createTextUtils(JRos1ClientConfiguration config) {
        var utils = new TextUtils();
        if (config.getMaxMessageLoggingLength() != -1)
            utils.withEllipsize(config.getMaxMessageLoggingLength());
        return utils;
    }
}
