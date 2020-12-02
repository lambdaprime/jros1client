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
/**
 * <p><b>jrosclient</b> is a Java module which allows to interact with
 * ROS (Robotic Operation System).</p>
 * 
 * <p>Many people run their applications, webservices in Java. The problems they
 * may encounter is when they try to interact from their existing logic with
 * ROS. What sounds as easy thing to do is in fact complicated because there
 * is not many Java based ROS clients. Some of them already outdated other
 * require additional knowledge of catkin, Python or running a separate web service.</p>
 * 
 * <p>This module is focused on ROS for Java and not ROS for Android. That is why
 * it is based on Java 11 API (which is not available in Android). Its
 * ultimate goal is to implement
 * <a href="http://wiki.ros.org/Implementing%20Client%20Libraries">ROS client libraries requirements</a>
 * </p>
 * 
 * <p>ROS computation graph model is based on the nodes which can publish messages to the
 * certain topics and subscribe to them. To represent this model in Java <b>jrosclient</b>
 * relies on Java Flow API which corresponds to <a href="http://www.reactive-streams.org/">reactive-streams specification</a>.
 * 
 */
module jrosclient {
    requires xmlrpc.client;
    requires xmlrpc.server;
    requires id.xfunction;  
    requires id.kineticstreamer;
    requires id.ICE;
    requires xmlrpc.common;
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
    
    exports id.jrosclient.ros to jrosclient.tests;
    exports id.jrosclient.ros.transport to jrosclient.tests;
    exports id.jrosclient.ros.transport.io to jrosclient.tests;
    exports id.jrosmessages.impl to jrosclient.tests;

}
