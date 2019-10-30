module jrosclient {
    requires xmlrpc.client;
    requires xmlrpc.server;
    exports id.jrosclient;
    exports id.jrosclient.ros;
    exports id.jrosclient.ros.responses;
}