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
package id.jros1client.ros.responses;

import id.jros1client.ros.entities.Protocol;
import id.xfunction.XJson;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class ProtocolParamsResponse extends Response {

    public String name;
    public String host;
    public int port;

    public ProtocolParamsResponse withProtocol(Protocol protocol) {
        this.name = protocol.protocolName;
        return this;
    }

    public ProtocolParamsResponse withHost(String host) {
        this.host = host;
        return this;
    }

    public ProtocolParamsResponse withPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public String toString() {
        return XJson.merge(
                super.toString(), XJson.asString("name", name, "host", host, "port", port));
    }
}
