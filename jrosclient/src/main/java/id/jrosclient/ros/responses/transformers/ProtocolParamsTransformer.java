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
package id.jrosclient.ros.responses.transformers;

import id.jrosclient.ros.api.impl.RawResponse;
import id.jrosclient.ros.responses.ProtocolParamsResponse;

public class ProtocolParamsTransformer implements ResponseTransformer {

    public ProtocolParamsResponse parse(RawResponse response) {
        ProtocolParamsResponse ss = new ProtocolParamsResponse();
        ResponseTransformer.populate(ss, response);
        RawResponse protocolParams = response.get(2);
        ss.name = protocolParams.get(0).string();
        ss.host = protocolParams.get(1).string();
        ss.port = protocolParams.get(2).integer();
        return ss;
    }
    
    public RawResponse transform(ProtocolParamsResponse response) {
        var raw = new RawResponse(new Object[3]);
        ResponseTransformer.populate(raw, response);
        raw.list().set(2, new Object[]{response.name, response.host, response.port});
        return raw;
    }
}
