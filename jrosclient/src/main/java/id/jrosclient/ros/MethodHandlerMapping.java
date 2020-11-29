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