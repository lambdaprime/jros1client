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
package id.jrosmessages.geometry_msgs;

import java.util.Objects;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

/**
 * Definition for geometry_msgs/Point32
 */
@MessageMetadata(
    type = Point32Message.NAME,
    md5sum = "cc153912f1453b708d221682bc23d9ac")
public class Point32Message implements Message {

    static final String NAME = "geometry_msgs/Point32";

    @Streamed
    public float x, y, z;

    public Point32Message() {
    }

    public Point32Message(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point32Message withX(float x) {
        this.x = x;
        return this;
    }

    public Point32Message withY(float y) {
        this.y = y;
        return this;
    }

    public Point32Message withZ(float z) {
        this.z = z;
        return this;
    }

    @Override
    public String toString() {
        return XJson.asString("x", x,
                "y", y,
                "z", z).toString();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
    
    @Override
    public boolean equals(Object obj) {
        Point32Message other = (Point32Message) obj;
        return Objects.equals(x, other.x)
                && Objects.equals(y, other.y)
                && Objects.equals(z, other.z);
    }
}
