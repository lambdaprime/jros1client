module jrosclient {
    requires xmlrpc.client;
    requires xmlrpc.server;
    requires id.xfunction;
    requires id.kineticstreamer;
    requires xmlrpc.common;
    requires java.logging;
    exports id.jrosclient;
    exports id.jrosclient.ros;
    exports id.jrosclient.ros.api;
    exports id.jrosclient.ros.transport;
    exports id.jrosclient.ros.entities;
    exports id.jrosclient.ros.responses;
    exports id.jrosclient.ros.messages.std_msgs.msg to id.kineticstreamer;
}
