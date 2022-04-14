/*
 * Copyright 2020 jrosclient project
 * 
 * Website: https://github.com/lambdaprime/jros1client
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package id.jros1client.ros.api.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/** @author lambdaprime intid@protonmail.com */
public class RawResponse implements Iterable<RawResponse> {

    private Object obj;

    public RawResponse(Object obj) {
        this.obj = obj;
    }

    public <T> List<T> list() {
        T[] a = (T[]) obj;
        return Arrays.asList(a);
    }

    public RawResponse get(int i) {
        return new RawResponse(((Object[]) obj)[i]);
    }

    public String string() {
        return (String) obj;
    }

    public int integer() {
        return (Integer) obj;
    }

    @Override
    public Iterator<RawResponse> iterator() {
        Object[] a = (Object[]) obj;
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

    public Object getObject() {
        return obj;
    }
}
