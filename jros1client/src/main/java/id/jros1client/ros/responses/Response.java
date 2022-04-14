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
package id.jros1client.ros.responses;

import id.xfunction.XJson;

/**
 * Base class for all ROS API returned values.
 *
 * @see <a href="http://wiki.ros.org/ROS/Master_Slave_APIs">Return value format</a>
 * @author lambdaprime intid@protonmail.com
 */
public class Response {

    public enum StatusCode {
        ERROR(-1),
        FAILURE(0),
        SUCCESS(1);

        private int code;

        public int code() {
            return code;
        }
        ;

        StatusCode(int code) {
            this.code = code;
        }

        public static StatusCode valueOf(int code) {
            switch (code) {
                case -1:
                    return StatusCode.ERROR;
                case 0:
                    return StatusCode.FAILURE;
                case 1:
                    return StatusCode.SUCCESS;
                default:
                    throw new RuntimeException();
            }
        }
    }

    public StatusCode statusCode;
    public String statusMessage;

    public Response withStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public Response withStatusMessage(String message) {
        this.statusMessage = message;
        return this;
    }

    @Override
    public String toString() {
        return XJson.asString(
                "statusCode", statusCode,
                "statusMessage", statusMessage);
    }
}
