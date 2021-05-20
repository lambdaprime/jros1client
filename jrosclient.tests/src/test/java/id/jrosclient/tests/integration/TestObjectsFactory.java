package id.jrosclient.tests.integration;

import id.jrosclient.JRosClientConfiguration;
import id.jrosclient.impl.ObjectsFactory;
import id.jrosclient.impl.TextUtils;
import id.jrosclient.ros.NodeServer;
import id.jrosclient.ros.transport.PublishersManager;
import id.jrosclient.ros.transport.TcpRosServer;

public class TestObjectsFactory extends ObjectsFactory {

    NodeServer nodeServer;
    TcpRosServer tcpRosServer;

    @Override
    public NodeServer createNodeServer(JRosClientConfiguration config) {
        if (nodeServer == null)
            nodeServer = super.createNodeServer(config);
        return nodeServer;
    }

    @Override
    public TcpRosServer createTcpRosServer(PublishersManager publishersManager, JRosClientConfiguration config,
            TextUtils textUtils) {
        if (tcpRosServer == null)
            tcpRosServer = super.createTcpRosServer(publishersManager, config, textUtils);
        return tcpRosServer;
    }

}
