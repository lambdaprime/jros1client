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
package id.jrosclient.ros.entities;

import id.jrosclient.ros.api.impl.Utils;
import java.util.List;

/** @author lambdaprime intid@protonmail.com */
public class Publisher implements Entity {

    public String topic;
    public List<String> topicPublisher;

    public Publisher(String topic, List<String> topicPublisher) {
        this.topic = topic;
        this.topicPublisher = topicPublisher;
    }

    @Override
    public String toString() {
        return String.format(
                "{ \"topic\": \"%s\", \"topicPublisher\": %s }",
                topic, Utils.asArrayOfStrings(topicPublisher));
    }
}
