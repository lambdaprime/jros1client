module jrosclient {
    requires xmlrpc.client;
    requires xmlrpc.server;
    requires id.ICE;
    exports id.jrosclient;
    exports id.jrosclient.ros;
    exports id.jrosclient.ros.responses;
}