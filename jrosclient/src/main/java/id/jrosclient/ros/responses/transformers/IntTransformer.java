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
package id.jrosclient.ros.responses.transformers;

import id.jrosclient.ros.api.impl.RawResponse;
import id.jrosclient.ros.responses.IntResponse;

public class IntTransformer implements ResponseTransformer {

    public IntResponse parse(String key, RawResponse response) {
        IntResponse ss = new IntResponse(key);
        ResponseTransformer.populate(ss, response);
        ss.value = response.get(2).integer();
        return ss;
    }

    public RawResponse transform(IntResponse response) {
        var raw = new RawResponse(new Object[3]);
        ResponseTransformer.populate(raw, response);
        raw.list().set(2, response.value);
        return raw;
    }
}
