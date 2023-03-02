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

import id.jros1client.impl.Settings;
import id.jros1client.ros.transport.io.ConnectionHeaderWriter;
import id.jros1client.ros.transport.io.MessagePacketReader;
import id.jrosclient.JRosClient;
import id.jrosclient.exceptions.JRosClientException;
import id.jrosclient.utils.TextUtils;
import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadataAccessor;
import id.xfunction.Preconditions;
import id.xfunction.concurrent.NamedThreadFactory;
import id.xfunction.logging.TracingToken;
import id.xfunction.logging.XLogger;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Connector establishes TCPROS connection with publishing ROS node and calls {@link Processor} to
 * process messages from it in background thread.
 *
 * <p>Connector is not used by {@link JRosClient} publishers but by {@link JRosClient} subscribers
 * instead.
 *
 * @author lambdaprime intid@protonmail.com
 */
public class TcpRosClientConnector<M extends Message, C extends ConnectionHeader>
        implements AutoCloseable {

    /**
     * Once connection with another node is established the control over TCPROS channel will be
     * given over to {@link Processor}
     *
     * @author lambdaprime intid@protonmail.com
     */
    public static interface Processor<C extends ConnectionHeader> {
        MessagePacketReader<C> newMessagePacketReader(DataInputStream dis);

        ConnectionHeaderWriter<C> newConnectionHeaderWriter(DataOutputStream dos);

        boolean isStopped();

        /** Process next message from the channel unless it is {@link Processor#isStopped} */
        void processNextMessage() throws Exception;

        /** Unrecoverable error, {@link #processNextMessage()} will not be called anymore */
        void onError(Exception e);
    }

    private String callerId;
    private String host;
    private int port;
    private Class<M> messageClass;
    private DataOutputStream dos;
    private DataInputStream dis;
    private ConnectionHeaderWriter<C> writer;
    private MessagePacketReader<C> reader;
    private ExecutorService executorService;
    private SocketChannel channel;

    private XLogger logger;
    private TextUtils utils;
    private Processor<C> proc;
    private boolean isClosed;

    public TcpRosClientConnector(
            @SuppressWarnings("exports") TracingToken tracingToken,
            String callerId,
            String topic,
            String host,
            int port,
            Class<M> messageClass,
            TextUtils utils,
            Processor<C> proc) {
        logger = XLogger.getLogger(getClass(), tracingToken);
        this.callerId = callerId;
        this.host = host;
        this.port = port;
        this.messageClass = messageClass;
        executorService =
                Executors.newSingleThreadExecutor(
                        new NamedThreadFactory("tcp-ros-client-" + topic.replace("/", "")));
        this.utils = utils;
        this.proc = proc;
    }

    public void connect(C ch) throws IOException {
        MessageMetadataAccessor metadataAccessor = new MessageMetadataAccessor();
        ch.withCallerId(callerId)
                .withType(metadataAccessor.getName(messageClass))
                .withMd5Sum(metadataAccessor.getMd5(messageClass));
        Preconditions.isTrue(channel == null, "Already connected");
        channel = SocketChannel.open(new InetSocketAddress(host, port));
        dis = new DataInputStream(Channels.newInputStream(channel));
        dos = new DataOutputStream(new BufferedOutputStream(Channels.newOutputStream(channel)));
        writer = proc.newConnectionHeaderWriter(dos);
        reader = proc.newMessagePacketReader(dis);
        executorService.execute(
                () -> {
                    try {
                        handshake(ch);
                        while (!executorService.isShutdown() && !proc.isStopped())
                            proc.processNextMessage();
                    } catch (Exception e) {
                        if (isClosed && e instanceof AsynchronousCloseException) {
                            // connector was closed while Processor is reading
                            // we can ignore this error
                            return;
                        }
                        logger.warning(
                                "Subscriber failed: {0}: {1}",
                                e.getClass().getSimpleName(), e.getMessage());
                        proc.onError(e);
                    } finally {
                        executorService.shutdown();
                    }
                });
    }

    @Override
    public void close() {
        logger.entering("close");

        isClosed = true;
        try {
            channel.close();
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(
                    Settings.getInstance().getAwaitTcpRosClientInSecs(), TimeUnit.SECONDS)) {
                logger.log(Level.FINE, "Forcefully terminating executor");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.severe(e.getMessage());
        }
        logger.exiting("close");
    }

    protected C handshake(C header) throws IOException {
        logger.log(Level.FINE, "Connection header: {0}", utils.toString(header));
        writer.write(header);
        var responseHeader = reader.readHeader();
        logger.log(
                Level.FINE,
                "Handshake response connection header: {0}",
                utils.toString(responseHeader));
        var errorOpt = responseHeader.error;
        if (errorOpt.isPresent()) {
            throw new JRosClientException(
                    "Handshake failed, ROS publisher node returned error response: {0}",
                    errorOpt.get());
        }
        return responseHeader;
    }
}
