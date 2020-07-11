package id.jrosclient;

import java.net.MalformedURLException;

import id.jrosclient.impl.RosRpcClient;
import id.jrosclient.ros.api.MasterApi;
import id.jrosclient.ros.api.NodeApi;
import id.jrosclient.ros.api.impl.MasterApiImpl;
import id.jrosclient.ros.api.impl.NodeApiImpl;

public class JRosClient {

    private MasterApi masterApi;

    public JRosClient(String masterUrl) throws MalformedURLException {
        RosRpcClient client = new RosRpcClient(masterUrl);
        masterApi = new MasterApiImpl(client);
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

}
