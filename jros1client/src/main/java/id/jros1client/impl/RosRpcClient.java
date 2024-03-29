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

import id.jros1client.ros.api.impl.RawResponse;
import id.xfunction.logging.XLogger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Supplier;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * @hidden
 * @author lambdaprime intid@protonmail.com
 */
public class RosRpcClient {

    private static final XLogger LOGGER = XLogger.getLogger(RosRpcClient.class);
    private String url;
    private Supplier<XmlRpcClient> client =
            () -> {
                var config = new XmlRpcClientConfigImpl();
                try {
                    config.setServerURL(new URL(url));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                var cl = new XmlRpcClient();
                cl.setConfig(config);
                client = () -> cl;
                return cl;
            };

    public RosRpcClient(String url) {
        this.url = url;
    }

    public RawResponse execute(String method, Object[] params) {
        LOGGER.fine("Executing ROS RPC request: {0}", method);
        try {
            return new RawResponse(client.get().execute(method, params));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
