module jrosclient {
    requires xmlrpc.client;
    requires id.ICE;
    exports id.jrosclient;
    exports id.jrosclient.ros;
    exports id.jrosclient.ros.responses;
}