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
/**
 * <p>Java module which allows to interact with
 * ROS (Robotic Operation System).</p>
 * 
 * @see <a href="https://github.com/lambdaprime/jrosclient/releases">Download</a>
 * @see <a href="https://github.com/lambdaprime/jrosclient">GitHub repository</a>
 * 
 */
module jrosclient {
    requires xmlrpc.client;
    requires xmlrpc.server;
    requires xmlrpc.common;
    requires id.xfunction;  
    requires id.kineticstreamer;
    requires id.ICE;
    requires java.logging;
    
    exports id.jrosclient;
    exports id.jrosclient.ros.api;
    exports id.jrosclient.ros.entities;
    exports id.jrosclient.ros.responses;
    
    exports id.jrosmessages;
    exports id.jrosmessages.primitives;
    exports id.jrosmessages.std_msgs;
    exports id.jrosmessages.geometry_msgs;
    exports id.jrosmessages.visualization_msgs;
    exports id.jrosmessages.sensor_msgs;
    exports id.jrosmessages.trajectory_msgs;
    exports id.jrosmessages.shape_msgs;
    exports id.jrosmessages.object_recognition_msgs;
    exports id.jrosmessages.octomap_msgs;

    exports id.jrosclient.impl to jrosclient.tests;
    exports id.jrosclient.ros to jrosclient.tests;
    exports id.jrosclient.ros.transport to jrosclient.tests;
    exports id.jrosclient.ros.transport.io to jrosclient.tests;
    exports id.jrosmessages.impl to jrosclient.tests;

}
