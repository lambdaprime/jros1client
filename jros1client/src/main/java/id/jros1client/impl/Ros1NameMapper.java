/*
 * Copyright 2022 jrosclient project
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
package id.jros1client.impl;

import id.jrosclient.utils.RosNameUtils;
import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadataAccessor;

/**
 * Mapper for ROS1 topic names.
 *
 * @hidden - exclude from javadoc
 * @author lambdaprime intid@protonmail.com
 */
public class Ros1NameMapper {

    private static final String ACTION_GOAL = "ActionGoal";
    private static final String ACTION_RESULT = "ActionResult";
    private MessageMetadataAccessor metadataAccessor = new MessageMetadataAccessor();
    private RosNameUtils rosNameUtils;

    public Ros1NameMapper(RosNameUtils rosNameUtils) {
        this.rosNameUtils = rosNameUtils;
    }

    public <M extends Message> String asFullyQualifiedTopicName(
            String topicName, Class<M> messageClass) {
        var rosAbsoluteTopicName = rosNameUtils.toAbsoluteName(topicName);
        var interfaceType = metadataAccessor.getInterfaceType(messageClass);
        var name = metadataAccessor.getName(messageClass);
        return switch (interfaceType) {
            case MESSAGE, SERVICE -> rosAbsoluteTopicName;
            case ACTION -> {
                if (name.endsWith(ACTION_GOAL)) yield rosAbsoluteTopicName + "/goal";
                if (name.endsWith(ACTION_RESULT)) yield rosAbsoluteTopicName + "/result";
                throw new IllegalArgumentException(
                        "Not a valid message name " + name + " for an Action");
            }
            default -> throw new UnsupportedOperationException(
                    "ROS interface type " + interfaceType);
        };
    }
}
