package id.jrosclient;

import java.net.MalformedURLException;

import id.jrosclient.impl.RosRpcClient;
import id.jrosclient.ros.api.MasterApi;
import id.jrosclient.ros.api.NodeApi;
import id.jrosclient.ros.api.impl.MasterApiImpl;
import id.jrosclient.ros.api.impl.NodeApiImpl;
import id.jrosclient.ros.api.impl.NodeServer;

public class JRosClient implements AutoCloseable {

    private MasterApi masterApi;
    private NodeServer nodeServer;

    public JRosClient(String masterUrl, int nodePort) throws MalformedURLException {
        RosRpcClient client = new RosRpcClient(masterUrl);
        nodeServer = new NodeServer(nodePort);
        masterApi = new MasterApiImpl(client, nodeServer);
    }

    public MasterApi getMasterApi() {
        return masterApi;
    }

    /**
     * Returns Node API of the foreign node
     * @param nodeUrl URL of the foreign node to connect
     */
    public NodeApi getNodeApi(String nodeUrl) {
        RosRpcClient client = new RosRpcClient(nodeUrl);
        return new NodeApiImpl(client);
    }

    @Override
    public void close() throws Exception {
        nodeServer.close();
    }
}
