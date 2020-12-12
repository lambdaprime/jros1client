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
 * - lambdaprime <id.blackmesa@gmail.com>
 */
package id.jrosclient.app;

import java.util.HashMap;
import java.util.Map;

import id.jrosmessages.Message;
import id.jrosmessages.geometry_msgs.Point32Message;
import id.jrosmessages.geometry_msgs.PointMessage;
import id.jrosmessages.geometry_msgs.PoseMessage;
import id.jrosmessages.geometry_msgs.QuaternionMessage;
import id.jrosmessages.geometry_msgs.Vector3Message;
import id.jrosmessages.impl.MetadataAccessor;
import id.jrosmessages.std_msgs.ColorRGBAMessage;
import id.jrosmessages.std_msgs.HeaderMessage;
import id.jrosmessages.std_msgs.Int32Message;
import id.jrosmessages.std_msgs.StringMessage;
import id.jrosmessages.visualization_msgs.MarkerMessage;

/**
 * Directory of all message types available in the system.
 * It contains all predefined message types but it is possible to
 * add new ones.
 */
public class MessagesDirectory {

    private MetadataAccessor accessor = new MetadataAccessor();
    private final Map<String, Class<? extends Message>> messages = new HashMap<>();

    public MessagesDirectory() {
        add(StringMessage.class);
        add(PointMessage.class);
        add(Point32Message.class);
        add(QuaternionMessage.class);
        add(PoseMessage.class);
        add(HeaderMessage.class);
        add(ColorRGBAMessage.class);
        add(Vector3Message.class);
        add(MarkerMessage.class);
        add(Int32Message.class);
    }
    
    public Class<? extends Message> get(String messageType) {
        return messages.get(messageType);
    }
    
    public void add(Class<? extends Message> messageClass) {
        messages.put(accessor.getType(messageClass), messageClass);
    }
}
