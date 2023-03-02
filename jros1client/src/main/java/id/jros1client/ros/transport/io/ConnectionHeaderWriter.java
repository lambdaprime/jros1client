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

import static id.jros1client.ros.transport.ConnectionHeader.CALLER_ID;
import static id.jros1client.ros.transport.ConnectionHeader.LATCHING;
import static id.jros1client.ros.transport.ConnectionHeader.MD5_SUM;
import static id.jros1client.ros.transport.ConnectionHeader.MESSAGE_DEFINITION;
import static id.jros1client.ros.transport.ConnectionHeader.TOPIC;
import static id.jros1client.ros.transport.ConnectionHeader.TYPE;

import id.jros1client.ros.transport.ConnectionHeader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class ConnectionHeaderWriter<C extends ConnectionHeader> {

    private DataOutputStream out;
    private Utils utils = new Utils();

    public ConnectionHeaderWriter(DataOutputStream out) {
        this.out = out;
    }

    public void write(C header) throws IOException {
        if (header == ConnectionHeader.EMPTY) return;
        int totalLen = calcTotalLen(header);
        if (totalLen == 0) return;
        utils.writeLen(out, totalLen);
        writeAllFields(header);
        out.flush();
    }

    protected void writeAllFields(C header) throws IOException {
        writeField(CALLER_ID, header.callerId);
        writeField(TOPIC, header.topic);
        writeField(TYPE, header.type);
        writeField(MESSAGE_DEFINITION, header.messageDefinition);
        writeField(MD5_SUM, header.md5sum);
        writeField(LATCHING, header.latching);
    }

    protected void writeField(String field, Optional<String> value) throws IOException {
        if (value.isEmpty()) return;
        utils.writeLen(out, len(field, value));
        out.write(field.getBytes());
        out.write('=');
        out.write(value.get().getBytes());
    }

    protected int calcTotalLen(C header) {
        int totalLen = 0;
        int len = 0;

        len = len(CALLER_ID, header.callerId);
        if (len > 0) totalLen += len + 4;

        len = len(TOPIC, header.topic);
        if (len > 0) totalLen += len + 4;

        len = len(TYPE, header.type);
        if (len > 0) totalLen += len + 4;

        len = len(MESSAGE_DEFINITION, header.messageDefinition);
        if (len > 0) totalLen += len + 4;

        len = len(MD5_SUM, header.md5sum);
        if (len > 0) totalLen += len + 4;

        len = len(LATCHING, header.latching);
        if (len > 0) totalLen += len + 4;

        return totalLen;
    }

    public static int len(String field, Optional<String> value) {
        if (value.isEmpty()) return 0;
        int len = lenField(value, field.length());
        if (len == 0) return 0;
        len += 1; // '='
        return len;
    }

    private static int lenField(Optional<String> field, int len) {
        return field.map(String::length).orElse(0) + (field.isPresent() ? len : 0);
    }
}
