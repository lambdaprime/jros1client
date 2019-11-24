package id.jrosclient.ros.entities.transformers;

import java.util.Map;

import id.jrosclient.ros.entities.Entity;
import id.jrosclient.ros.entities.Publisher;

public class Transformers {

    public PublisherTransformer publisherTransformer = new PublisherTransformer();

//    private Map<Class<?>, Transformer<?>> transformers = Map.of(
//            Publisher.class, publisherTransformer );
//
//    public <E extends Entity> Transformer<E> getTransformer(Class<E> clazz) {
//        return (Transformer<E>) transformers.get(clazz);
//    }
}
