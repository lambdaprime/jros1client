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
package id.jrosmessages.std_msgs;

import java.util.Objects;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

/**
 * Definition for std_msgs/String
 */
@MessageMetadata(
    type = "std_msgs/String",
    md5sum = "992ce8a1687cec8c8bd883ec73ca41d1")
public class StringMessage implements Message {
    
    @Streamed
    public String data = "";

    public StringMessage withData(String data) {
        this.data = data;
        return this;
    }
    
    @Override
    public String toString() {
        return XJson.asString("data", data).toString();
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(data);
    }
    
    @Override
    public boolean equals(Object obj) {
        StringMessage other = (StringMessage) obj;
        return Objects.equals(data, other.data);
    }
}
