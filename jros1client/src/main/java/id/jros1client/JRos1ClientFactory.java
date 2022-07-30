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
package id.jros1client;

import id.jros1client.impl.ObjectsFactory;

/**
 * Factory methods for {@link JRos1Client}
 *
 * @author lambdaprime intid@protonmail.com
 */
public class JRos1ClientFactory {

    private static final ObjectsFactory objectsFactory = new ObjectsFactory();

    // visible for javadoc
    public static final String DEFAULT_ROS_MASTER_URL = "http://localhost:11311";

    /**
     * Create ROS1 client with default configuration connected to ROS master running locally using
     * URL {@link #DEFAULT_ROS_MASTER_URL}
     */
    public JRos1Client createClient() {
        return createClient(DEFAULT_ROS_MASTER_URL);
    }

    /**
     * Create ROS1 client with default configuration.
     *
     * @param masterUrl ROS1 master node URL
     */
    public JRos1Client createClient(String masterUrl) {
        return createClient(masterUrl, objectsFactory.createConfig());
    }

    /**
     * Creates ROS1 client connected to ROS master running locally using URL {@link
     * #DEFAULT_ROS_MASTER_URL}
     */
    public JRos1Client createClient(JRos1ClientConfiguration config) {
        return createClient(DEFAULT_ROS_MASTER_URL, config);
    }

    /**
     * Creates ROS1 client.
     *
     * @param masterUrl ROS1 master node URL
     */
    public JRos1Client createClient(String masterUrl, JRos1ClientConfiguration config) {
        return createClient(masterUrl, config, objectsFactory);
    }

    /**
     * @hidden visible for testing
     */
    public JRos1Client createClient(
            String masterUrl, JRos1ClientConfiguration config, ObjectsFactory objectsFactory) {
        return new JRos1Client(masterUrl, config, objectsFactory);
    }
}
