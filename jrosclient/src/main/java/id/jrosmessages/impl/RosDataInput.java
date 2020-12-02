/*
 * Copyright 2020 jrosclient project
 * 
 * Website: https://github.com/lambdaprime/jrosclient
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
/*
 * Authors:
 * - lambdaprime <id.blackmesa@gmail.com>
 */
package id.jrosmessages.impl;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Array;

import id.kineticstreamer.InputKineticStream;
import id.kineticstreamer.KineticStreamReader;

public class RosDataInput implements InputKineticStream {

    private DataInput in;

    public RosDataInput(DataInput in) {
        this.in = in;
    }

    @Override
    public int readInt() throws IOException {
        return Integer.reverseBytes(in.readInt());
    }

    @Override
    public String readString() throws IOException {
        int len = readLen();
        byte[] b = new byte[len];
        in.readFully(b);
        return new String(b);
    }

    public int readLen() throws IOException {
        return Integer.reverseBytes(in.readInt());
    }

    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(
                Long.reverseBytes(
                        Double.doubleToRawLongBits(in.readDouble())));
    }

    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(
                Integer.reverseBytes(
                        Float.floatToRawIntBits(in.readFloat())));
    }

    @Override
    public boolean readBool() throws IOException {
        return in.readBoolean();
    }

    @Override
    public Object[] readArray(Class<?> type) throws Exception {
        var array = (Object[])Array.newInstance(type, readLen());
        for (int i = 0; i < array.length; i++) {
            array[i] = new KineticStreamReader(this).read(type);
        }
        return array;
    }

    @Override
    public void close() throws Exception {
        // nothing to release
    }
}
