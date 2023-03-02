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
package id.jros1client.ros.transport;

import id.jros1client.ros.transport.io.ConnectionHeaderWriter;
import id.jros1client.ros.transport.io.DefaultConnectionHeaderReader;
import id.jros1client.ros.transport.io.MessagePacketReader;
import id.jros1messages.MessageSerializationUtils;
import id.jrosclient.utils.TextUtils;
import id.jrosmessages.Message;
import id.jrosmessages.RosInterfaceType;
import id.xfunction.Preconditions;
import id.xfunction.concurrent.SameThreadExecutorService;
import id.xfunction.logging.TracingToken;
import id.xfunction.logging.XLogger;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Level;

/**
 * TCPROS client which communicates with publishing ROS1 node using {@link RosInterfaceType#MESSAGE}
 * interface.
 *
 * <p>This client establishes TCPROS connection with publishing ROS node and listens for new
 * messages.
 *
 * <p>Every new message it receives from such node it publishes to its own JRosClient subscriber
 * which is subscribed to the publishing ROS node.
 *
 * <p>This client can serve only one JRosClient subscriber.
 *
 * <p>This client is not used by JRosClient publishers.
 *
 * @author lambdaprime intid@protonmail.com
 */
public class TcpRosClient<M extends Message> extends SubmissionPublisher<M>
        implements TcpRosClientConnector.Processor<ConnectionHeader>, AutoCloseable {

    private static final MessageSerializationUtils SERIALIZATION_UTILS =
            new MessageSerializationUtils();
    private Class<M> messageClass;
    private ConnectionHeaderWriter<ConnectionHeader> writer;
    private MessagePacketReader<ConnectionHeader> reader;
    private ConnectionHeader connectionHeader;
    private TcpRosClientConnector<M, ConnectionHeader> connector;
    private XLogger logger;
    private TextUtils utils;

    public TcpRosClient(
            @SuppressWarnings("exports") TracingToken tracingToken,
            String callerId,
            String topic,
            String host,
            int port,
            Class<M> messageClass,
            TextUtils utils) {
        super(new SameThreadExecutorService(), 1);
        this.messageClass = messageClass;
        this.utils = utils;
        tracingToken = new TracingToken(tracingToken, "" + hashCode());
        logger = XLogger.getLogger(getClass(), tracingToken);
        connector =
                new TcpRosClientConnector<>(
                        tracingToken, callerId, topic, host, port, messageClass, utils, this);
    }

    @Override
    public ConnectionHeaderWriter<ConnectionHeader> newConnectionHeaderWriter(
            DataOutputStream dos) {
        writer = new ConnectionHeaderWriter<ConnectionHeader>(dos);
        return writer;
    }

    @Override
    public MessagePacketReader<ConnectionHeader> newMessagePacketReader(DataInputStream dis) {
        reader = new MessagePacketReader<>(dis, new DefaultConnectionHeaderReader(dis));
        return reader;
    }

    @Override
    public void onError(Exception e) {
        logger.entering("sendOnError");
        logger.severe("Connection with ROS publisher is closed due to error", e);
        var subscribers = getSubscribers();
        if (!subscribers.isEmpty()) {
            Preconditions.equals(1, subscribers.size(), "Unexpected number of subscribers");
            subscribers.get(0).onError(e);
        }
        logger.exiting("sendOnError");
    }

    @Override
    public void processNextMessage() throws Exception {
        byte[] body = reader.readBody();
        logger.log(Level.FINE, "Next packet body: {0}", utils.toString(body));
        if (body.length > 0) {
            var msg = SERIALIZATION_UTILS.read(body, messageClass);
            logger.log(Level.FINE, "Submitting received message to subscriber");
            if (!isClosed()) submit(msg);
        } else {
            logger.log(Level.WARNING, "Received empty message data");
        }
        logger.log(Level.FINE, "Requesting next message");
        writer.write(connectionHeader);
    }

    @Override
    public void close() {
        logger.entering("close");

        // trying gracefully to close the client
        // first we need to issue onComplete to all subscribers
        super.close();

        // now we close the connection
        // we always need to do it after completing subscribers
        // otherwise some of them may still be reading from the socket
        // and report onError later
        connector.close();
    }

    @Override
    public boolean isStopped() {
        return !hasSubscribers() || isClosed();
    }

    public void connect(ConnectionHeader connectionHeader) throws IOException {
        this.connectionHeader = connectionHeader;
        connector.connect(connectionHeader);
    }
}
