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
package id.jrosmessages.visualization_msgs;

import java.util.Arrays;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

/**
 * Definition for visualization_msgs/MarkerArray
 */
@MessageMetadata(type = MarkerArrayMessage.NAME, md5sum = "d155b9ce5188fbaf89745847fd5882d7")
public class MarkerArrayMessage implements Message {

    static final String NAME = "visualization_msgs/MarkerArray";

    @Streamed
    public MarkerMessage[] markers = new MarkerMessage[0];

    public MarkerArrayMessage withMarkers(MarkerMessage... markers) {
        this.markers = markers;
        return this;
    }

    @Override
    public String toString() {
        return XJson.asString(
                "markers", Arrays.toString(markers));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(markers);
    }

    @Override
    public boolean equals(Object obj) {
        MarkerArrayMessage other = (MarkerArrayMessage) obj;
        return Arrays.equals(markers, other.markers);
    }
}
