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
package id.jros1client.ros.transport;

import id.xfunction.function.Unchecked;
import id.xfunction.io.XOutputStream;
import java.io.ByteArrayInputStream;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class MessagePacket {

    private ConnectionHeader header;
    private byte[] body;

    public MessagePacket(ConnectionHeader header) {
        this(header, new byte[0]);
    }

    public MessagePacket(ConnectionHeader header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public ConnectionHeader getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public String toString() {
        var out = new XOutputStream();
        Unchecked.run(() -> new ByteArrayInputStream(body).transferTo(out));
        return String.format("{ header: %s, body: [%s]}", header, out.asHexString());
    }
}
