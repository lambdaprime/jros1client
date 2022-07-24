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

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadataAccessor;
import id.xfunction.lang.XRuntimeException;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class ConnectionHeaderValidator {

    private MessageMetadataAccessor metadataAccessor;

    public ConnectionHeaderValidator(MessageMetadataAccessor metadataAccessor) {
        this.metadataAccessor = metadataAccessor;
    }

    public boolean validate(Class<? extends Message> messageClass, ConnectionHeader header) {
        if (header.getType().isEmpty()) {
            throw new XRuntimeException("Message %s type is empty", messageClass.getSimpleName());
        }
        var type = header.getType().get();

        var messageType = metadataAccessor.getName(messageClass);
        if (!messageType.equals(type)) {
            throw new XRuntimeException("Message type missmatch %s != %s", messageType, type);
        }

        if (header.getMd5sum().isEmpty()) {
            throw new XRuntimeException(
                    "Message %s MD5 sum is empty", messageClass.getSimpleName());
        }
        var md5sum = header.getMd5sum().get();

        var messageMd5 = metadataAccessor.getMd5(messageClass);
        if (!messageMd5.equals(md5sum)) {
            throw new XRuntimeException(
                    "Message %s md5 sum missmatch %s != %s",
                    messageClass.getSimpleName(), messageMd5, md5sum);
        }
        return true;
    }
}
