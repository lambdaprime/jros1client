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
package id.jrosclient.samples.polygon;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.jrosmessages.geometry_msgs.Point32Message;
import id.kineticstreamer.annotations.Streamed;

/**
 * Example of custom message definition
 */
@MessageMetadata(
    type = PolygonMessage.NAME,
    md5sum = "cd60a26494a087f577976f0329fa120e")
public class PolygonMessage implements Message {

    static final String NAME = "geometry_msgs/Polygon";

    @Streamed
    public Point32Message[] points = new Point32Message[0];

    public PolygonMessage withPoints(Point32Message[] points) {
        this.points = points;
        return this;
    }

}
