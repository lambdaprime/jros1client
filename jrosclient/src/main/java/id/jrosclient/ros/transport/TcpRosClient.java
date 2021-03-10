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
package id.jrosclient.ros.transport;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Level;
import java.util.logging.Logger;

import id.jrosclient.ros.transport.io.ConnectionHeaderWriter;
import id.jrosclient.ros.transport.io.MessagePacketReader;
import id.jrosmessages.Message;
import id.jrosmessages.impl.MessageTransformer;
import id.jrosmessages.impl.MetadataAccessor;
import id.xfunction.concurrent.NamedThreadFactory;
import id.xfunction.concurrent.SameThreadExecutorService;
import id.xfunction.logging.XLogger;

/**
 * Allows to communicate with other ROS nodes.
 */
public class TcpRosClient<M extends Message> extends SubmissionPublisher<M> implements AutoCloseable {

    private static final Logger LOGGER = XLogger.getLogger(TcpRosClient.class);
    
    private String callerId;
    private String topic;
    private String host;
    private int port;
    private Class<M> messageClass;
    private DataOutputStream dos;
    private DataInputStream dis;
    private ConnectionHeaderWriter writer;
    private MessagePacketReader reader;
    private ExecutorService executorService;

    public TcpRosClient(String callerId, String topic, String host, int port,
            Class<M> messageClass) {
        super(new SameThreadExecutorService(), 1);
        this.callerId = callerId;
        this.topic = topic;
        this.host = host;
        this.port = port;
        this.messageClass = messageClass;
        executorService = Executors.newSingleThreadExecutor(
                new NamedThreadFactory("tcp-ros-client-" + topic.replace("/", "")));
    }
    
    public void connect() throws IOException {
        SocketChannel channel = SocketChannel.open(new InetSocketAddress(host, port));
        OutputStream os = Channels.newOutputStream(channel);
        dis = new DataInputStream(Channels.newInputStream(channel));
        dos = new DataOutputStream(new BufferedOutputStream(os));
        writer = new ConnectionHeaderWriter(dos);
        reader = new MessagePacketReader(dis);
        MetadataAccessor metadataAccessor = new MetadataAccessor();
        String messageDefinition = "string data";
        var ch = new ConnectionHeader()
                .withTopic(topic.startsWith("/")? topic: "/" + topic)
                .withCallerId(callerId)
                .withType(metadataAccessor.getType(messageClass))
                .withMessageDefinition(messageDefinition )
                .withMd5Sum(metadataAccessor.getMd5(messageClass));
        executorService.execute(() -> {
            try {
                run(ch);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                executorService.shutdown();
            }
        });
    }

    private void run(ConnectionHeader header) throws Exception {
        writer.write(header);
        dos.flush();
        MessagePacket response = reader.read();
        LOGGER.log(Level.FINE, "Message packet: {0}", response);
        byte[] body = response.getBody();
        while (!executorService.isShutdown() && hasSubscribers()) {
            var msg = new MessageTransformer().transform(body, messageClass);
            LOGGER.log(Level.FINE, "Submitting received message to subscriber");
            submit(msg);
            LOGGER.log(Level.FINE, "Requesting next message");
            writer.write(header);
            dos.flush();
            body = reader.readBody();
        }
    }

    @Override
    public void close() {
        executorService.shutdown();
        super.close();
    }
}
