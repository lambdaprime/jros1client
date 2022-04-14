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

import id.ICE.scanners.MessageScanner;
import id.jros1client.ros.transport.io.Utils;
import id.xfunction.io.ByteBufferInputStream;
import id.xfunction.logging.XLogger;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/** @author lambdaprime intid@protonmail.com */
public class ConnectionHeaderScanner implements MessageScanner {

    private static final XLogger LOGGER = XLogger.getLogger(ConnectionHeaderScanner.class);
    private Utils utils = new Utils();

    @Override
    public int scan(ByteBuffer buffer) {
        LOGGER.entering("scan");
        int ret = scanInternal(buffer);
        LOGGER.exiting("scan", ret);
        return ret;
    }

    private int scanInternal(ByteBuffer buffer) {
        try {
            if (buffer.limit() < 4) return -1;
            var dis = new DataInputStream(new ByteBufferInputStream(buffer));
            var len = utils.readLen(dis) + 4;
            if (buffer.limit() < len) return -1;
            return len;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
