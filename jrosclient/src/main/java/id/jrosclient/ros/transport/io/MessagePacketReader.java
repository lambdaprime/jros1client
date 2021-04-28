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
package id.jrosclient.ros.transport.io;

import java.io.DataInput;
import java.io.IOException;

import id.jrosclient.ros.transport.ConnectionHeader;
import id.jrosclient.ros.transport.MessagePacket;

public class MessagePacketReader {

    private DataInput in;
    private ConnectionHeaderReader headerReader;
    private Utils utils = new Utils();

    public MessagePacketReader(DataInput input) {
        this.in = input;
        headerReader = new ConnectionHeaderReader(in);
    }

    public MessagePacket read() throws IOException {
        var ch = readHeader();
        byte[] b = readBody();
        return new MessagePacket(ch, b);
    }

    public ConnectionHeader readHeader() throws IOException {
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
