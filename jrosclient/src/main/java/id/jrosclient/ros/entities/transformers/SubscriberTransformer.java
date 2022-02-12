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
package id.jrosclient.ros.entities.transformers;

import id.jrosclient.ros.entities.Subscriber;

class SubscriberTransformer implements Transformer<Subscriber> {

    public Subscriber transform(Object obj) {
        Object[] a = (Object[]) obj;
        return new Subscriber((String) a[0],
                Transformer.list((Object[]) a[1]));
    }

    @Override
    public Object transform(Subscriber entity) {
        return null;
    }

}
