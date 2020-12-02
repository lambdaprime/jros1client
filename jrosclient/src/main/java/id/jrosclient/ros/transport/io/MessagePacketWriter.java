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
package id.jrosclient.ros.transport.io;

import java.io.DataOutput;
import java.io.IOException;

import id.jrosclient.ros.transport.MessagePacket;

public class MessagePacketWriter {

    private DataOutput out;
    private ConnectionHeaderWriter headerWriter;
    private Utils utils = new Utils();
    
    public MessagePacketWriter(DataOutput output) {
        this.out = output;
        headerWriter = new ConnectionHeaderWriter(output);
    }

    public void write(MessagePacket packet) throws IOException {
        headerWriter.write(packet.getHeader());
        var body = packet.getBody();
        if (body == null) return;
        utils.writeLen(out, body.length);
        out.write(body);
    }

}
