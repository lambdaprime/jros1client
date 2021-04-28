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
package id.jrosclient.ros.entities;

import java.util.List;

import id.xfunction.XJson;

public class Protocol implements Entity {

    public static Protocol TCPROS = new Protocol("TCPROS");

    public String protocolName;
    public List<String> protocolParams = List.of();

    public Protocol(String name) {
        protocolName = name;
    }

    public String getProtocolName() {
        return protocolName;
    }
    
    @Override
    public String toString() {
        return XJson.asString("name", protocolName);
    }
}
