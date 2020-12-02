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
 * - lambdaprime <id.blackmesa@gmail.com>
 */
package id.jrosclient.tests;

import id.jrosclient.ros.transport.ConnectionHeader;
import id.xfunction.function.Unchecked;
import id.xfunction.XUtils;

public class ConnectionHeaderSamples {

    public static class ConnectionHeaderSample {
        private final String resource;
        private final ConnectionHeader header;
        
        ConnectionHeaderSample(String resource, ConnectionHeader header) {
            this.resource = resource;
            this.header = header;
        }
    
        String getResource() {
            return resource;
        }
    
        ConnectionHeader getHeader() {
            return header;
        }
    }
    
    private static final String MESSAGE_DATA = "string data";

    static final ConnectionHeaderSample HEADER = new ConnectionHeaderSample("connection_header", new ConnectionHeader()
            .withTopic("/topic")
            .withCallerId("jrosclient")
            .withType("std_msgs/String")
            .withMessageDefinition(MESSAGE_DATA)
            .withMd5Sum(Unchecked.get(() -> XUtils.md5Sum(MESSAGE_DATA))));

}
