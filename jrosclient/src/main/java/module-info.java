module jrosclient {
    requires xmlrpc.client;
    requires xmlrpc.server;
    requires xfunction;
    exports id.jrosclient;
    exports id.jrosclient.ros;
    exports id.jrosclient.ros.responses;
}