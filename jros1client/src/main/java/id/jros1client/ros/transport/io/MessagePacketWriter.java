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
package id.jros1client.ros.transport.io;

import id.jros1client.ros.transport.ConnectionHeader;
import id.jros1client.ros.transport.MessagePacket;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class MessagePacketWriter<C extends ConnectionHeader> {

    private DataOutputStream out;
    private ConnectionHeaderWriter<C> headerWriter;
    private Utils utils = new Utils();

    public MessagePacketWriter(DataOutputStream output) {
        this.out = output;
        headerWriter = new ConnectionHeaderWriter<>(output);
    }

    public void write(MessagePacket packet) throws IOException {
        headerWriter.write((C) packet.getHeader());
        var body = packet.getBody();
        if (body == null) return;
        utils.writeLen(out, body.length);
        out.write(body);
        out.flush();
    }
}
