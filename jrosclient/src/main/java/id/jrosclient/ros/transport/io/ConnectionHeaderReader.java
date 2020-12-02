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

import java.io.DataInput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import id.jrosclient.ros.transport.ConnectionHeader;

public class ConnectionHeaderReader {

    private DataInput in;
    private Utils utils = new Utils();

    public ConnectionHeaderReader(DataInput in) {
        this.in = in;
    }

    public ConnectionHeader read() throws IOException {
        int len = utils.readLen(in);
        var map = new HashMap<String, String>();
        while (len != 0) {
            int fieldLen = utils.readLen(in);
            var field = readField(fieldLen);
            map.put(field.getKey(), field.getValue());
            len -= 4 + fieldLen;
        }
        var ch = new ConnectionHeader();
        map.entrySet().stream()
            .forEach(e -> ch.add(e.getKey(), e.getValue()));
        return ch;
    }

    private Entry<String, String> readField(int fieldLen) throws IOException {
        byte[] buf = new byte[fieldLen];
        in.readFully(buf);
        var a = new String(buf).split("=");
        return Map.entry(a[0], a[1]);
    }

}
