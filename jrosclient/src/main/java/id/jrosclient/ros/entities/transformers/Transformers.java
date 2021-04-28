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

import id.jrosclient.ros.entities.Protocol;
import id.jrosclient.ros.entities.Publisher;
import id.jrosclient.ros.entities.Subscriber;

public class Transformers {

    public Transformer<Publisher> publisherTransformer = new PublisherTransformer();
    public Transformer<Protocol> protocolTransformer = new ProtocolTransformer();
    public Transformer<Subscriber> subscriberTransformer = new SubscriberTransformer();

//    private Map<Class<?>, Transformer<?>> transformers = Map.of(
//            Publisher.class, publisherTransformer );
//
//    public <E extends Entity> Transformer<E> getTransformer(Class<E> clazz) {
//        return (Transformer<E>) transformers.get(clazz);
//    }
}
