package id.jrosclient.melodic;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import id.jrosclient.RosRpcClient;
import id.jrosclient.melodic.impl.MasterApiImpl;

public class JRosClient {

    private MasterApi masterApi;

    private JRosClient(RosRpcClient rpcClient) {
        masterApi = new MasterApiImpl(rpcClient);
    }

    public MasterApi getMasterApi() {
        return masterApi;
    }

    public static JRosClient create(String url) throws MalformedURLException {
        RosRpcClient client = new RosRpcClient(url);
        return new JRosClient(client);
    }
}
