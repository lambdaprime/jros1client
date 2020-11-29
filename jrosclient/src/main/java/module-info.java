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
