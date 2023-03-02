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
package id.jros1client;

import id.jros1client.impl.Ros1NameMapper;
import id.jros1client.impl.RosRpcClient;
import id.jros1client.ros.NodeServer;
import id.jros1client.ros.api.MasterApi;
import id.jros1client.ros.api.NodeApi;
import id.jros1client.ros.api.impl.MasterApiClientImpl;
import id.jros1client.ros.api.impl.NodeApiClientImpl;
import id.jros1client.ros.entities.Protocol;
import id.jros1client.ros.transport.ConnectionHeader;
import id.jros1client.ros.transport.PublishersManager;
import id.jros1client.ros.transport.TcpRosClient;
import id.jros1client.ros.transport.TcpRosServer;
import id.jrosclient.JRosClient;
import id.jrosclient.RosVersion;
import id.jrosclient.TopicPublisher;
import id.jrosclient.exceptions.JRosClientException;
import id.jrosclient.utils.RosNameUtils;
import id.jrosclient.utils.TextUtils;
import id.jrosmessages.Message;
import id.jrosmessages.MessageMetadataAccessor;
import id.xfunction.concurrent.flow.MergeProcessor;
import id.xfunction.function.Unchecked;
import id.xfunction.logging.TracingToken;
import id.xfunction.logging.XLogger;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Flow.Subscriber;

/**
 * Main class of the library which allows to interact with ROS1.
 *
 * <p>Each instance of {@link JRos1Client} acts as a separate ROS node and listens to its own ports.
 *
 * @author lambdaprime intid@protonmail.com
 */
public class JRos1Client implements JRosClient {

    private static final Ros1NameMapper nameMapper = new Ros1NameMapper(new RosNameUtils());

    private XLogger logger;

    private String masterUrl;
    private NodeServer nodeServer;
    private TcpRosServer tcpRosServer;
    private MessageMetadataAccessor metadataAccessor = new MessageMetadataAccessor();
    private Set<TcpRosClient<?>> clients = new HashSet<>();
    private PublishersManager publishersManager;
    private JRos1ClientConfiguration configuration;
    private TextUtils textUtils;
    private TracingToken tracingToken;

    JRos1Client(
            TracingToken tracingToken,
            String masterUrl,
            JRos1ClientConfiguration config,
            NodeServer nodeServer,
            TextUtils textUtils,
            TcpRosServer tcpRosServer,
            PublishersManager publishersManager) {
        this.tracingToken = tracingToken;
        this.masterUrl = masterUrl;
        configuration = config;
        this.nodeServer = nodeServer;
        this.textUtils = textUtils;
        this.tcpRosServer = tcpRosServer;
        this.publishersManager = publishersManager;
        logger = XLogger.getLogger(getClass(), tracingToken);
    }

    /** Access to ROS master API */
    public MasterApi getMasterApi() {
        RosRpcClient client = new RosRpcClient(masterUrl);
        return new MasterApiClientImpl(client);
    }

    /**
     * Return Node API of the foreign node. It allows to interact with other ROS nodes directly.
     *
     * @param nodeUrl URL of the foreign node to connect
     */
    public NodeApi getNodeApi(String nodeUrl) {
        RosRpcClient client = new RosRpcClient(nodeUrl);
        return new NodeApiClientImpl(client);
    }

    @Override
    public <M extends Message> void subscribe(
            String topic, Class<M> messageClass, Subscriber<M> subscriber)
            throws JRosClientException {
        topic = nameMapper.asFullyQualifiedTopicName(topic, messageClass);
        var topicType = metadataAccessor.getName(messageClass);
        var callerId = configuration.getCallerId();
        var publishers =
                getMasterApi()
                        .registerSubscriber(
                                callerId, topic, topicType, configuration.getNodeApiUrl());
        logger.fine("Publishers: {0}", publishers);
        if (publishers.value.isEmpty()) {
            throw new JRosClientException("No publishers for topic %s found", topic);
        }

        // closed once subscriber cancels subscription
        @SuppressWarnings("resource")
        var processor = new MergeProcessor<M>();
        processor.subscribe(subscriber);
        for (var publisher : publishers.value) {
            try {
                logger.fine("Registering with publisher: {0}", publisher);
                var nodeApi = getNodeApi(publisher);
                var protocol = nodeApi.requestTopic(callerId, topic, List.of(Protocol.TCPROS));
                logger.fine("Protocol configuration: {0}", protocol);
                var nodeClient =
                        new TcpRosClient<M>(
                                tracingToken,
                                callerId,
                                topic,
                                protocol.host,
                                protocol.port,
                                messageClass,
                                textUtils);
                nodeClient.subscribe(processor.newSubscriber());
                // jrosclient does not send real message definition and instead relies on its md5
                // sum
                String messageDefinition = "string data";
                nodeClient.connect(
                        new ConnectionHeader()
                                .withTopic(topic)
                                .withMessageDefinition(messageDefinition));
                clients.add(nodeClient);
            } catch (Exception e) {
                logger.fine("Failed to register with publisher: {0}", e.getMessage());
            }
        }
    }

    @Override
    public <M extends Message> void publish(TopicPublisher<M> publisher)
            throws JRosClientException {
        var topic =
                nameMapper.asFullyQualifiedTopicName(
                        publisher.getTopic(), publisher.getMessageClass());
        var clazz = publisher.getMessageClass();
        var topicType = metadataAccessor.getName(clazz);
        publishersManager.add(topic, publisher);
        try {
            tcpRosServer.start();
        } catch (IOException e) {
            throw new JRosClientException(e);
        }
        nodeServer.start();
        var subscribers =
                getMasterApi()
                        .registerPublisher(
                                configuration.getCallerId(),
                                topic,
                                topicType,
                                configuration.getNodeApiUrl());
        logger.fine("Current subscribers: {0}", subscribers.toString());
    }

    /**
     * Unregister publisher in Master node and stop publisher from emitting new messages
     *
     * @param topic name of the topic used by the publisher
     */
    @Override
    public <M extends Message> void unpublish(String topic, Class<M> messageClass)
            throws JRosClientException {
        topic = nameMapper.asFullyQualifiedTopicName(topic, messageClass);
        var publisherOpt = publishersManager.getPublisher(topic);
        if (publisherOpt.isEmpty()) {
            logger.fine("There is no publishers for topic {0}, nothing to unpublish", topic);
            return;
        }
        var publisher = publisherOpt.get();
        try {
            publisher.close();
        } catch (IOException e) {
            logger.severe("Failed to close topic publisher ", e);
        }
        try {
            var num =
                    getMasterApi()
                            .unregisterPublisher(
                                    configuration.getCallerId(),
                                    topic,
                                    configuration.getNodeApiUrl());
            logger.fine("Unregistered publisher response: {0}", num.toString());
        } finally {
            publishersManager.remove(topic);
        }
    }

    @Override
    public boolean hasPublisher(String topic) {
        return getMasterApi().getSystemState(configuration.getCallerId()).publishers.stream()
                .anyMatch(p -> topic.equals(p.topic));
    }

    /**
     * {@inheritDoc}
     *
     * <p>Release the resources, stop TCPROS server and node server
     */
    @Override
    public void close() {
        try {
            var exception = new RuntimeException();
            publishersManager.getPublishers().stream()
                    .forEach(
                            Unchecked.wrapAccept(
                                    pub -> unpublish(pub.getTopic(), pub.getMessageClass()),
                                    exception));
            if (exception.getSuppressed().length != 0) throw exception;
        } finally {
            nodeServer.close();
            clients.forEach(Unchecked.wrapAccept(TcpRosClient::close));
            tcpRosServer.close();
            clients.clear();
        }
    }

    @Override
    public EnumSet<RosVersion> getSupportedRosVersion() {
        return EnumSet.of(RosVersion.ROS1);
    }

    /** {@link JRos1Client} implementation specific configuration */
    public JRos1ClientConfiguration getClientConfiguration() {
        return configuration;
    }
}
