package id.jrosclient;

import java.net.MalformedURLException;

import id.jrosclient.impl.RosRpcClient;
import id.jrosclient.ros.MasterApi;
import id.jrosclient.ros.impl.MasterApiImpl;
import id.jrosclient.ros.impl.NodeServer;

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

    @Override
    public void close() throws Exception {
        nodeServer.close();
    }
}
