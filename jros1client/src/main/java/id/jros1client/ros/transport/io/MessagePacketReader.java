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
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class MessagePacketReader<C extends ConnectionHeader> {

    private DataInputStream in;
    private ConnectionHeaderReader<C> headerReader;
    private Utils utils = new Utils();

    public MessagePacketReader(DataInputStream input, ConnectionHeaderReader<C> headerReader) {
        this.in = input;
        this.headerReader = headerReader;
    }

    public MessagePacket read() throws IOException {
        var ch = readHeader();
        if (ch.error.isPresent()) return new MessagePacket(ch);
        if (in.available() == 0) return new MessagePacket(ch);
        byte[] b = readBody();
        return new MessagePacket(ch, b);
    }

    public C readHeader() throws IOException {
        return headerReader.read();
    }

    public byte[] readBody() throws IOException {
        byte[] b = readBody(utils.readLen(in));
        return b;
    }

    private byte[] readBody(int bodyLen) throws IOException {
        byte[] buf = new byte[bodyLen];
        in.readFully(buf);
        return buf;
    }
}
