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
import id.jrosclient.ros.responses.ListResponse;
import id.jrosclient.ros.responses.Response.StatusCode;

public class ListTransformer implements ResponseTransformer {

    public ListResponse<String> parseString(String key, RawResponse response) {
        ListResponse<String> ss = new ListResponse<>(key);
        ResponseTransformer.populate(ss, response);
        if (ss.statusCode == StatusCode.ERROR) {
            return ss;
        }
        for (RawResponse r : response.get(2)) {
            ss.value.add(r.string());
        }
        return ss;
    }

}
