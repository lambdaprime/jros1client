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

import java.util.Arrays;
import java.util.Objects;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.jrosmessages.std_msgs.HeaderMessage;
import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

/**
 * Definition for sensor_msgs/PointCloud2
 */
@MessageMetadata(type = PointCloud2Message.NAME, md5sum = "1158d486dd51d683ce2f1be655c3c181")
public class PointCloud2Message implements Message {

    static final String NAME = "sensor_msgs/PointCloud2";

    /**
     * Time of sensor data acquisition, and the coordinate frame ID (for 3d points)
     */
    @Streamed
    public HeaderMessage header = new HeaderMessage();

    /**
     * 2D structure of the point cloud. If the cloud is unordered, height is 1 and
     * width is the length of the point cloud (row_step / point_step).
     */
    @Streamed
    public int height;

    @Streamed
    public int width;

    /**
     * Describes the channels and their layout in the binary data blob.
     */
    @Streamed
    public PointFieldMessage[] fields = new PointFieldMessage[0];

    /**
     * Is this data bigendian?
     */
    @Streamed
    public boolean is_bigendian;

    /**
     * Length of a point in bytes
     */
    @Streamed
    public int point_step;

    /**
     * Length of a row in bytes
     */
    @Streamed
    public int row_step;

    /**
     * Actual point data, size is (row_step*height)
     */
    @Streamed
    public byte[] data = new byte[0];

    /**
     * True if there are no invalid points
     */
    @Streamed
    public boolean is_dense;

    public PointCloud2Message() {
    }

    public PointCloud2Message withHeader(HeaderMessage header) {
        this.header = header;
        return this;
    }

    public PointCloud2Message withData(byte[] data) {
        this.data = data;
        return this;
    }

    public PointCloud2Message withFields(PointFieldMessage... fields) {
        this.fields = fields;
        return this;
    }

    public PointCloud2Message withHeight(int height) {
        this.height = height;
        return this;
    }

    public PointCloud2Message withWidth(int width) {
        this.width = width;
        return this;
    }

    public PointCloud2Message withIsBigendian(boolean is_bigendian) {
        this.is_bigendian = is_bigendian;
        return this;
    }

    public PointCloud2Message withIsDense(boolean is_dense) {
        this.is_dense = is_dense;
        return this;
    }

    public PointCloud2Message withPointStep(int step) {
        this.point_step = step;
        return this;
    }

    public PointCloud2Message withRowStep(int step) {
        this.row_step = step;
        return this;
    }

    @Override
    public String toString() {
        return XJson.asString(
                "header", header,
                "height", height,
                "width", width,
                "is_bigendian", is_bigendian,
                "is_dense", is_dense,
                "point_step", point_step,
                "row_step", row_step,
                "fields", Arrays.toString(fields),
                "data", Arrays.toString(data));
    }

    @Override
    public int hashCode() {
        return Objects.hash(header,
                height,
                width,
                is_bigendian,
                is_dense,
                point_step,
                row_step,
                Arrays.hashCode(fields),
                Arrays.hashCode(data));
    }

    @Override
    public boolean equals(Object obj) {
        PointCloud2Message other = (PointCloud2Message) obj;
        return Objects.equals(header, other.header)
                && Objects.equals(height, other.height)
                && Objects.equals(width, other.width)
                && Objects.equals(is_bigendian, other.is_bigendian)
                && Objects.equals(is_dense, other.is_dense)
                && Objects.equals(point_step, other.point_step)
                && Objects.equals(row_step, other.row_step)
                && Arrays.equals(fields, other.fields)
                && Arrays.equals(data, other.data);
    }
}
