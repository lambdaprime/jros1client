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
package id.jrosclient.ros.transport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import id.jrosclient.TopicPublisher;
import id.jrosmessages.Message;
import id.xfunction.XRE;
import id.xfunction.function.Unchecked;

public class PublishersManager implements AutoCloseable {

    // topic name to its publisher
    private Map<String, TopicPublisher<?>> publishers = new HashMap<>();
    
    public <M extends Message> void add(TopicPublisher<M> publisher) {
        if (publishers.containsKey(publisher.getTopic()))
            throw new XRE("Publisher for topic %s already exist", publisher.getTopic());
        publishers.put(publisher.getTopic(), publisher);
    }
    
    public Optional<TopicPublisher<?>> getPublisher(String topic) {
        return Optional.ofNullable(publishers.get(topic));
    }

    public void remove(String topic) {
        publishers.remove(topic);
    }
    
    public List<TopicPublisher<?>> getPublishers() {
        return List.copyOf(publishers.values());
    }

    @Override
    public void close() throws Exception {
        publishers.values().forEach(Unchecked.wrapAccept(TopicPublisher::close));
        publishers.clear();
    }
}
