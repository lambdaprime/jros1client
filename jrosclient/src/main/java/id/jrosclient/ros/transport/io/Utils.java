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
package id.jrosclient.ros.transport.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Utils {

    public int readLen(DataInput in) throws IOException {
        return Integer.reverseBytes(in.readInt());
    }
    
    public void writeLen(DataOutput out, int len) throws IOException {
        out.writeInt(Integer.reverseBytes(len));
    }
}
