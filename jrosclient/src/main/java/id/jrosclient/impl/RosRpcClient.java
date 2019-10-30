package id.jrosclient.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Supplier;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import id.jrosclient.ros.impl.RawResponse;

public class RosRpcClient {

    private String url;
    private Supplier<XmlRpcClient> client = () -> {
        var config = new XmlRpcClientConfigImpl();
        try {
            config.setServerURL(new URL(url));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        var cl = new XmlRpcClient();
        cl.setConfig(config);
        client = () -> cl;
        return cl;
    };

    public RosRpcClient(String url) {
        this.url = url;
    }

    public RawResponse execute(String method, Object[] params) {
        try {
            return new RawResponse(client.get().execute(method, params));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
