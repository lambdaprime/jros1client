package id.jrosclient.ros.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class RawResponse implements Iterable<RawResponse> {

    private Object obj;

    public RawResponse(Object obj) {
        this.obj = obj;
    }
    
    public <T> List<T> list() {
        T[] a = (T[])obj;
        return Arrays.asList(a);
    }

    public RawResponse get(int i) {
        return new RawResponse(((Object[])obj)[i]);
    }

    public String string() {
        return (String)obj;
    }

    public int integer() {
        return (Integer)obj;
    }

    @Override
    public Iterator<RawResponse> iterator() {
        Object[] a = (Object[])obj;
        return new Iterator<RawResponse>() {
            int i = 0;
            @Override
            public boolean hasNext() {
                return i < a.length;
            }

            @Override
            public RawResponse next() {
                return new RawResponse(a[i++]);
            }
        };
    }
}
