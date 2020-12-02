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
package id.jrosmessages.visualization_msgs;

import java.util.Arrays;
import java.util.Objects;

import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadata;
import id.jrosmessages.geometry_msgs.PointMessage;
import id.jrosmessages.geometry_msgs.PoseMessage;
import id.jrosmessages.geometry_msgs.Vector3Message;
import id.jrosmessages.primitives.Duration;
import id.jrosmessages.std_msgs.ColorRGBAMessage;
import id.jrosmessages.std_msgs.HeaderMessage;
import id.jrosmessages.std_msgs.StringMessage;
import id.kineticstreamer.annotations.Streamed;
import id.xfunction.XJson;

/**
 * Definition for visualization_msgs/Marker
 */
@MessageMetadata(
    type = MarkerMessage.NAME,
    md5sum = "4048c9de2a16f4ae8e0538085ebf1b97")
public class MarkerMessage implements Message {

    static final String NAME = "visualization_msgs/Marker";

    public enum Type {
        ARROW,
        CUBE,
        SPHERE,
        CYLINDER,
        LINE_STRIP,
        LINE_LIST,
        CUBE_LIST,
        SPHERE_LIST,
        POINTS,
        TEXT_VIEW_FACING,
        MESH_RESOURCE,
        TRIANGLE_LIST
    }
    
    public enum Action {
        ADD,
        MODIFY,
        DELETE,
        DELETEALL,
    }

    /**
     * Header for time/frame information
     */
    @Streamed
    public HeaderMessage header = new HeaderMessage();

    /**
     * Namespace to place this object in... used in conjunction
     * with id to create a unique name for the object
     */
    @Streamed
    public StringMessage ns = new StringMessage();
    
    /**
     * Object ID useful in conjunction with the namespace
     * for manipulating and deleting the object later
     */
    @Streamed
    public int id;

    /**
     * Type of object
     */
    @Streamed
    public int type;
    
    /**
     * 0 add/modify an object, 1 (deprecated), 2 deletes an
     * object, 3 deletes all objects
     */
    @Streamed
    public int action;
    
    /**
     * Pose of the object
     */
    @Streamed
    public PoseMessage pose = new PoseMessage();;

    /**
     * Scale of the object 1,1,1 means default (usually 1 meter square)
     */
    @Streamed
    public Vector3Message scale = new Vector3Message();

    /**
     * Color [0.0-1.0]
     */
    @Streamed
    public ColorRGBAMessage color = new ColorRGBAMessage();

    /**
     * How long the object should last before being automatically deleted. 
     * 0 means forever
     */
    @Streamed
    public Duration lifetime = new Duration();

    /**
     * If this marker should be frame-locked, i.e. retransformed into its
     * frame every timestep
     */
    @Streamed
    public boolean frame_locked;
    
    /**
     * Only used if the type specified has some use for them (eg. POINTS, LINE_STRIP, ...)
     */
    @Streamed
    public PointMessage[] points = new PointMessage[0];

    /**
     * Only used if the type specified has some use for them (eg. POINTS, LINE_STRIP, ...)
     * number of colors must either be 0 or equal to the number of points
     * NOTE: alpha is not yet used
     */
    @Streamed
    public ColorRGBAMessage[] colors = new ColorRGBAMessage[0];

    /**
     * Only used for text markers
     */
    @Streamed
    public StringMessage mesh_resource = new StringMessage();;
    
    /**
     * Only used for MESH_RESOURCE markers
     */
    @Streamed
    public StringMessage text = new StringMessage();;
    
    /**
     * If this marker should be frame-locked, i.e. retransformed into its
     * frame every timestep
     */
    @Streamed
    public boolean mesh_use_embedded_materials;
    
    public MarkerMessage withHeader(HeaderMessage header) {
        this.header = header;
        return this;
    }
    
    public MarkerMessage withNs(StringMessage ns) {
        this.ns = ns;
        return this;
    }
    
    public MarkerMessage withId(int id) {
        this.id = id;
        return this;
    }
    
    public MarkerMessage withType(Type type) {
        this.type = type.ordinal();
        return this;
    }
    
    public MarkerMessage withAction(Action action) {
        this.action = action.ordinal();
        return this;
    }
    
    public MarkerMessage withPose(PoseMessage pose) {
        this.pose = pose;
        return this;
    }
    
    public MarkerMessage withScale(Vector3Message scale) {
        this.scale = scale;
        return this;
    }
    
    public MarkerMessage withColor(ColorRGBAMessage color) {
        this.color = color;
        return this;
    }
    
    public MarkerMessage withLifetime(Duration lifetime) {
        this.lifetime = lifetime;
        return this;
    }
    
    public MarkerMessage withFrameLocked(boolean frame_locked) {
        this.frame_locked = frame_locked;
        return this;
    }

    @Override
    public String toString() {
        return XJson.asString(
                "header", header,
                "ns", ns,
                "id", id,
                "type", type,
                "action", action,
                "pose", pose,
                "scale", scale,
                "color", color,
                "lifetime", lifetime,
                "frame_locked", frame_locked,
                "points", Arrays.asList(points),
                "colors", Arrays.asList(colors),
                "mesh_resource", mesh_resource,
                "text", text,
                "mesh_use_embedded_materials", mesh_use_embedded_materials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header,
                ns,
                id,
                type,
                action,
                pose,
                scale,
                color,
                lifetime,
                frame_locked,
                Arrays.hashCode(points),
                Arrays.hashCode(colors),
                mesh_resource,
                text,
                mesh_use_embedded_materials);
    }

    @Override
    public boolean equals(Object obj) {
        MarkerMessage other = (MarkerMessage) obj;
        return Objects.equals(header, other.header)
                && Objects.equals(ns, other.ns)
                && Objects.equals(id, other.id)
                && Objects.equals(type, other.type)
                && Objects.equals(action, other.action)
                && Objects.equals(pose, other.pose)
                && Objects.equals(scale, other.scale)
                && Objects.equals(color, other.color)
                && Objects.equals(lifetime, other.lifetime)
                && Objects.equals(frame_locked, other.frame_locked)
                && Arrays.equals(points, other.points)
                && Arrays.equals(colors, other.colors)
                && Objects.equals(mesh_resource, other.mesh_resource)
                && Objects.equals(text, other.text)
                && mesh_use_embedded_materials == other.mesh_use_embedded_materials;
    }
}
