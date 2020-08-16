package id.jrosclient.ros.entities.transformers;

import id.jrosclient.ros.entities.Protocol;

public class ProtocolTransformer implements Transformer<Protocol> {

    @Override
    public Object transform(Protocol protocol) {
        Object[] obj = new Object[2];
        obj[0] = protocol.protocolName;
        obj[1] = protocol.protocolParams.toArray();
        return obj;
    }

    @Override
    public Protocol transform(Object protocol) {
        Object[] a = (Object[]) protocol;
        return new Protocol((String) a[0]);
    }

}
