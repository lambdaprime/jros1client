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
 * - lambdaprime <intid@protonmail.com>
 */
package id.jrosclient.ros;

import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.server.XmlRpcHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcNoSuchHandlerException;

import id.xfunction.logging.XLogger;

/**
 * Receives an XmlRpcRequest, extracts method name and arguments
 * from it and then calls this method on a HANDLER object using
 * {@link MethodCaller}
 */
final class MethodHandlerMapping implements XmlRpcHandlerMapping {

    private static final Logger LOGGER = XLogger.getLogger(NodeServer.class);
    private MethodCaller caller;
    private XmlRpcHandler handler = new XmlRpcHandler() {
        @Override
        public Object execute(XmlRpcRequest request) {
            var methodName = request.getMethodName();
            var args = IntStream.range(0, request.getParameterCount())
                    .mapToObj(i -> request.getParameter(i))
                    .collect(Collectors.toList());
            try {
                return caller.call(methodName, args);
            } catch (NoSuchMethodException e) {
                LOGGER.severe("Operation " + methodName + " is not supported, ignoring...");
                return null;
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }
    };
    
    public MethodHandlerMapping(Object handler) throws Exception {
        caller = new MethodCaller(handler);
    }

    @Override
    public XmlRpcHandler getHandler(String arg0)
            throws XmlRpcNoSuchHandlerException, XmlRpcException 
    {
        return handler;
    }
    
}
