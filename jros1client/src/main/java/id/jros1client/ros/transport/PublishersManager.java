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

import id.jrosclient.TopicPublisher;
import id.jrosmessages.Message;
import id.xfunction.function.Unchecked;
import id.xfunction.lang.XRE;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author lambdaprime intid@protonmail.com
 */
public class PublishersManager implements AutoCloseable {

    // topic name to its publisher
    private Map<String, TopicPublisher<?>> publishers = new HashMap<>();

    public <M extends Message> void add(String topic, TopicPublisher<M> publisher) {
        if (publishers.containsKey(topic))
            throw new XRE("Publisher for topic %s already exist", topic);
        publishers.put(topic, publisher);
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
