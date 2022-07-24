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
import id.jrosclient.JRosClient;

/**
 * Factory methods for {@link JRos1Client}
 *
 * @author lambdaprime intid@protonmail.com
 */
public class JRos1ClientFactory {

    private static final ObjectsFactory objectsFactory = new ObjectsFactory();

    // visible for javadoc
    public static final String DEFAULT_ROS_MASTER_URL = "http://localhost:11311";

    /** Create client with default configuration */
    public JRosClient createJRosClient() {
        return createSpecializedJRos1Client();
    }

    /**
     * Specialized ROS clients ideally should be avoided since they make your code to rely on
     * specific version of ROS which means that it will not be compatible with all other ROS
     * versions.
     *
     * @param masterUrl master node URL
     */
    public JRosClient createJRosClient(String masterUrl) {
        return createSpecializedJRos1Client(masterUrl, objectsFactory.createConfig());
    }

    /** Creates client with given configuration */
    public JRosClient createJRosClient(JRos1ClientConfiguration config) {
        return createSpecializedJRos1Client(config);
    }

    /**
     * @hidden visible for testing
     */
    public JRos1Client createJRosClient(String masterUrl, JRos1ClientConfiguration config) {
        return createSpecializedJRos1Client(masterUrl, config);
    }

    /**
     * @hidden visible for testing
     */
    public JRos1Client createJRosClient(
            String masterUrl, JRos1ClientConfiguration config, ObjectsFactory objectsFactory) {
        return createSpecializedJRos1Client(masterUrl, config, objectsFactory);
    }

    /**
     * Creates specialized ROS1 client to ROS master running locally using URL {@link
     * #DEFAULT_ROS_MASTER_URL}
     *
     * <p>Specialized ROS clients ideally should be avoided since they make your code to rely on
     * specific version of ROS which means that it will not be compatible with all other ROS
     * versions.
     */
    public JRos1Client createSpecializedJRos1Client() {
        return createSpecializedJRos1Client(DEFAULT_ROS_MASTER_URL);
    }

    /**
     * Specialized ROS clients ideally should be avoided since they make your code to rely on
     * specific version of ROS which means that it will not be compatible with all other ROS
     * versions.
     *
     * @param masterUrl master node URL
     */
    public JRos1Client createSpecializedJRos1Client(String masterUrl) {
        return createSpecializedJRos1Client(masterUrl, objectsFactory.createConfig());
    }

    /**
     * Creates specialized ROS1 client to ROS master running locally using URL {@link
     * #DEFAULT_ROS_MASTER_URL} with given client configuration
     *
     * <p>Specialized ROS clients ideally should be avoided since they make your code to rely on
     * specific version of ROS which means that it will not be compatible with all other ROS
     * versions.
     */
    public JRos1Client createSpecializedJRos1Client(JRos1ClientConfiguration config) {
        return createSpecializedJRos1Client(DEFAULT_ROS_MASTER_URL, config);
    }

    /**
     * Specialized ROS clients ideally should be avoided since they make your code to rely on
     * specific version of ROS which means that it will not be compatible with all other ROS
     * versions.
     */
    public JRos1Client createSpecializedJRos1Client(
            String masterUrl, JRos1ClientConfiguration config) {
        return createSpecializedJRos1Client(masterUrl, config, objectsFactory);
    }

    private JRos1Client createSpecializedJRos1Client(
            String masterUrl, JRos1ClientConfiguration config, ObjectsFactory objectsFactory) {
        return new JRos1Client(masterUrl, config, objectsFactory);
    }
}
