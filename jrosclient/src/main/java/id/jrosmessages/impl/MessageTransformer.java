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
package id.jrosmessages.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import id.jrosmessages.Message;
import id.kineticstreamer.KineticStreamReader;
import id.kineticstreamer.KineticStreamWriter;

/**
 * Performs message (de)serialization (from)to stream of bytes.
 */
public class MessageTransformer {

    /**
     * Deserialize message from byte stream
     * @param <M> type of the message
     * @param data byte array with the message
     * @param clazz message class
     */
    public <M extends Message> M transform(byte[] data, Class<M> clazz) {
        try {
            var dis = new DataInputStream(new ByteArrayInputStream(data));
            var ks = new KineticStreamReader(new RosDataInput(dis));
            Object obj = ks.read(clazz);
            return (M) obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Serialize message to byte stream
     * @param message message to serialize
     */
    public byte[] transform(Message message) {
        try {
            var bos = new ByteArrayOutputStream();
            var dos = new DataOutputStream(bos);
            var ks = new KineticStreamWriter(new RosDataOutput(dos));
            ks.write(message);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
