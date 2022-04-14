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
import id.jros1client.ros.entities.Publisher;
import id.jros1client.ros.entities.Subscriber;
import id.jros1client.ros.entities.transformers.Transformer;
import id.jros1client.ros.responses.SystemStateResponse;

/** @author lambdaprime intid@protonmail.com */
public class SystemStateTransformer implements ResponseTransformer {

    private Transformer<Publisher> publisherTransformer;
    private Transformer<Subscriber> subscriberTransformer;

    public SystemStateTransformer(
            Transformer<Publisher> publisherTransformer,
            Transformer<Subscriber> subscriberTransformer) {
        this.publisherTransformer = publisherTransformer;
        this.subscriberTransformer = subscriberTransformer;
    }

    public SystemStateResponse parse(RawResponse response) {
        SystemStateResponse ss = new SystemStateResponse();
        ResponseTransformer.populate(ss, response);
        RawResponse systemState = response.get(2);
        for (RawResponse publisher : systemState.get(0)) {
            ss.publishers.add(publisherTransformer.transform(publisher.getObject()));
        }
        for (RawResponse subscriber : systemState.get(1)) {
            ss.subscribers.add(subscriberTransformer.transform(subscriber.getObject()));
        }
        return ss;
    }
}
