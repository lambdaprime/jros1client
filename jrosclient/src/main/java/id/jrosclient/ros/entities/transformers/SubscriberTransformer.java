package id.jrosclient.ros.entities.transformers;

import id.jrosclient.ros.entities.Subscriber;

class SubscriberTransformer implements Transformer<Subscriber> {

    public Subscriber transform(Object obj) {
        Object[] a = (Object[]) obj;
        return new Subscriber((String)a[0],
                Transformer.list((Object[])a[1]));
    }

    @Override
    public Object transform(Subscriber entity) {
        return null;
    }

}
