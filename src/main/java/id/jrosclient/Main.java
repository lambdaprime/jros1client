package id.jrosclient;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class Main {

    public static void main(String[] args) throws MalformedURLException, Exception {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://ubuntu:11311/"));
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        Object[] params = new Object[]{"gggg"};
        Object[] result = (Object[]) client.execute("getSystemState", params);
        int code = (Integer)result[0];
        String statusMessage = (String)result[1];
    }
}
