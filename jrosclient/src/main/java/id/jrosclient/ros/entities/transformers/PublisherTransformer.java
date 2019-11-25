package id.jrosclient.ros.entities.transformers;

import id.jrosclient.ros.entities.Publisher;

class PublisherTransformer implements Transformer<Publisher> {

    public Publisher transform(Object obj) {
        Object[] a = (Object[]) obj;
        return new Publisher((String)a[0],
                Transformer.list((Object[])a[1]));
    }

    @Override
    public Object transform(Publisher entity) {
        // TODO Auto-generated method stub
        return null;
    }

}
