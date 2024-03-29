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
package id.jros1client.ros.entities.transformers;

import id.jros1client.ros.entities.Entity;
import java.util.Arrays;
import java.util.List;

/**
 * @author lambdaprime intid@protonmail.com
 */
public interface Transformer<E extends Entity> {

    Object transform(E entity);

    E transform(Object obj);

    /**
     * You cannot convert Object[] to String[]. But you can convert Object[] to T[] where T is
     * String.
     */
    static <T> List<T> list(Object[] a) {
        return Arrays.asList((T[]) a);
    }
}
