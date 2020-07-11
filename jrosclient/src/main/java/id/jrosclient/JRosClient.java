package id.jrosclient;

import java.net.MalformedURLException;

import id.jrosclient.impl.RosRpcClient;
import id.jrosclient.ros.api.MasterApi;
import id.jrosclient.ros.api.NodeApi;
import id.jrosclient.ros.api.impl.MasterApiClientImpl;
import id.jrosclient.ros.api.impl.NodeApiClientImpl;

public class JRosClient {

    private MasterApi masterApi;

    public JRosClient(String masterUrl) throws MalformedURLException {
        RosRpcClient client = new RosRpcClient(masterUrl);
        masterApi = new MasterApiClientImpl(client);
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
        return new NodeApiClientImpl(client);
    }

}
