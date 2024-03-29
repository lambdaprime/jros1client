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
package id.jros1client.ros.entities.transformers;

import id.jros1client.ros.entities.Protocol;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class ProtocolTransformer implements Transformer<Protocol> {

    @Override
    public Object transform(Protocol protocol) {
        Object[] obj = new Object[2];
        obj[0] = protocol.protocolName;
        obj[1] = protocol.protocolParams.toArray();
        return obj;
    }

    @Override
    public Protocol transform(Object protocol) {
        Object[] a = (Object[]) protocol;
        return new Protocol((String) a[0]);
    }
}
