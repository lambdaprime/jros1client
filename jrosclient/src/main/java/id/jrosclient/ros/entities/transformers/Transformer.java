package id.jrosclient.ros.entities.transformers;

import java.util.Arrays;
import java.util.List;

import id.jrosclient.ros.entities.Entity;

public interface Transformer<E extends Entity> {

    Object transform(E entity);
    E transform(Object obj);

    /**
     * You cannot convert Object[] to String[].
     * But you can convert Object[] to T[] where T is String.
     */
    static <T> List<T> list(Object[] a) {
        return Arrays.asList((T[])a);
    }

}
