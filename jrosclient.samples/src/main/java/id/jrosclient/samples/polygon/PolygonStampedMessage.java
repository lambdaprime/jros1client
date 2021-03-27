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
package id.jrosclient.samples.polygon;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.jrosmessages.std_msgs.HeaderMessage;
import id.kineticstreamer.annotations.Streamed;

/**
 * Example of custom message definition
 */
@MessageMetadata(
    type = PolygonStampedMessage.NAME,
    md5sum = "c6be8f7dc3bee7fe9e8d296070f53340")
public class PolygonStampedMessage implements Message {

    static final String NAME = "geometry_msgs/PolygonStamped";

    @Streamed
    public HeaderMessage header = new HeaderMessage();
    
    @Streamed
    public PolygonMessage polygon = new PolygonMessage();

    public PolygonStampedMessage withPolygon(PolygonMessage polygon) {
        this.polygon = polygon;
        return this;
    }

    public PolygonStampedMessage withHeader(HeaderMessage header) {
        this.header = header;
        return this;
    }

}