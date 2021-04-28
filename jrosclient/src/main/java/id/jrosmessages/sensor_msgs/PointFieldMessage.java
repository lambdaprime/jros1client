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
package id.jrosmessages.sensor_msgs;

import java.util.Objects;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

/**
 * Definition for sensor_msgs/PointField
 */
@MessageMetadata(
    type = PointFieldMessage.NAME,
    md5sum = "268eacb2962780ceac86cbd17e328150")
public class PointFieldMessage implements Message {

    static final String NAME = "sensor_msgs/PointField";

    /**
     * This message holds the description of one point entry in the
     * PointCloud2 message format.
     */
    public enum DataType {
        INT8,
        UINT8,
        INT16,
        UINT16,
        INT32,
        UINT32,
        FLOAT32,
        FLOAT64
    }
    
    /**
     * Name of field: x, y, z, rgb, rgba, etc.
     * @see <a href="http://docs.ros.org/hydro/api/pcl/html/point__types_8hpp.html">Point type names</a>
     */
    @Streamed
    public String name;
    
    /**
     * Offset from start of point struct
     */
    @Streamed
    public int offset;
    
    /**
     * Data type
     */
    @Streamed
    public byte datatype;
    
    /**
     * How many elements in the field
     */
    @Streamed
    public int count;
    
    public PointFieldMessage() {
        
    }
    
    public PointFieldMessage withName(String name) {
        this.name = name;
        return this;
    }
    
    public PointFieldMessage withOffset(int offset) {
        this.offset = offset;
        return this;
    }
    
    public PointFieldMessage withCount(int count) {
        this.count = count;
        return this;
    }
    
    public PointFieldMessage withDataType(DataType datatype) {
        this.datatype = (byte) datatype.ordinal();
        return this;
    }
    
    @Override
    public String toString() {
        return XJson.asString(
                "name", name,
                "offset", offset,
                "count", count,
                "datatype", DataType.values()[datatype]);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name,
                offset,
                count,
                datatype);
    }

    @Override
    public boolean equals(Object obj) {
        PointFieldMessage other = (PointFieldMessage) obj;
        return Objects.equals(name, other.name)
                && Objects.equals(offset, other.offset)
                && Objects.equals(count, other.count)
                && Objects.equals(datatype, other.datatype);
    }
}
