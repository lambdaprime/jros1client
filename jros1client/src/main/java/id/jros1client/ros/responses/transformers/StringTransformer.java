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
package id.jros1client.ros.responses.transformers;

import id.jros1client.ros.api.impl.RawResponse;
import id.jros1client.ros.responses.StringResponse;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class StringTransformer implements ResponseTransformer {

    public StringResponse parse(String key, RawResponse response) {
        StringResponse ss = new StringResponse(key);
        ResponseTransformer.populate(ss, response);
        ss.value = response.get(2).string();
        return ss;
    }
}
