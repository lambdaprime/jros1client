package id.jrosclient.ros.entities.transformers;

import id.jrosclient.ros.entities.Protocol;
import id.jrosclient.ros.entities.Publisher;

public class Transformers {

    public Transformer<Publisher> publisherTransformer = new PublisherTransformer();
    public Transformer<Protocol> protocolTransformer = new ProtocolTransformer();

//    private Map<Class<?>, Transformer<?>> transformers = Map.of(
//            Publisher.class, publisherTransformer );
//
//    public <E extends Entity> Transformer<E> getTransformer(Class<E> clazz) {
//        return (Transformer<E>) transformers.get(clazz);
//    }
}
