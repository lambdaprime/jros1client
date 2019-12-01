module jrosclient {
    requires xmlrpc.client;
    requires xmlrpc.server;
    requires xfunction;
    requires xmlrpc.common;
    exports id.jrosclient;
    exports id.jrosclient.ros.api;
    exports id.jrosclient.ros.entities;
    exports id.jrosclient.ros.responses;
}