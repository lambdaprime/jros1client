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
package id.jrosclient;

import id.jrosclient.impl.ObjectsFactory;
import id.jrosclient.impl.RosRpcClient;
import id.jrosclient.impl.TextUtils;
import id.jrosclient.impl.Utils;
import id.jrosclient.ros.NodeServer;
import id.jrosclient.ros.api.MasterApi;
import id.jrosclient.ros.api.NodeApi;
import id.jrosclient.ros.api.impl.MasterApiClientImpl;
import id.jrosclient.ros.api.impl.NodeApiClientImpl;
import id.jrosclient.ros.entities.Protocol;
import id.jrosclient.ros.transport.PublishersManager;
import id.jrosclient.ros.transport.TcpRosClient;
import id.jrosclient.ros.transport.TcpRosServer;
import id.jrosmessages.Message;
import id.jrosmessages.MetadataAccessor;
import id.xfunction.concurrent.flow.MergeProcessor;
import id.xfunction.function.Unchecked;
import id.xfunction.lang.XRE;
import id.xfunction.logging.XLogger;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class of the library which allows to interact with ROS.
 *
 * <p>Each instance of JRosClient acts as a separate ROS node and listens to its own ports.
 *
 * @author lambdaprime intid@protonmail.com
 */
public class JRosClient implements AutoCloseable {

    private static final ObjectsFactory objectsFactory = new ObjectsFactory();
    private static final Utils utils = new Utils();

    // visible for javadoc
    public static final String DEFAULT_ROS_MASTER_URL = "http://localhost:11311";

    private final Logger LOGGER = XLogger.getLogger(this);

    private String masterUrl;
    private NodeServer nodeServer;
    private TcpRosServer tcpRosServer;
    private MetadataAccessor metadataAccessor = new MetadataAccessor();
    private Set<TcpRosClient<?>> clients = new HashSet<>();
    private PublishersManager publishersManager = new PublishersManager();
    private JRosClientConfiguration configuration;
    private TextUtils textUtils;

    /**
     * Default constructor which creates a client to ROS master running locally using URL {@link
     * #DEFAULT_ROS_MASTER_URL}
     */
    public JRosClient() {
        this(DEFAULT_ROS_MASTER_URL);
    }

    /** @param masterUrl master node URL */
    public JRosClient(String masterUrl) {
        this(masterUrl, objectsFactory.createConfig());
    }

    /**
     * Constructor which creates a client to ROS master running locally using URL {@link
     * #DEFAULT_ROS_MASTER_URL} with given client configuration
     */
    public JRosClient(JRosClientConfiguration config) {
        this(DEFAULT_ROS_MASTER_URL, config);
    }

    public JRosClient(String masterUrl, JRosClientConfiguration config) {
        this(masterUrl, config, objectsFactory);
    }

    /** @hidden */
    public JRosClient(String masterUrl, JRosClientConfiguration config, ObjectsFactory factory) {
        this.masterUrl = masterUrl;
        nodeServer = factory.createNodeServer(config);
        textUtils = factory.createTextUtils(config);
        tcpRosServer = factory.createTcpRosServer(publishersManager, config, textUtils);
        configuration = config;
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

    /**
     * Subscribe to ROS topic
     *
     * @param <M> type of messages in the topic
     * @param subscriber provides information about the topic to subscribe for. Once subscribed it
     *     will be notified for any new message which gets published with given topic.
     */
    public <M extends Message> void subscribe(TopicSubscriber<M> subscriber) throws Exception {
        String topic = subscriber.getTopic();
        var clazz = subscriber.getMessageClass();
        var topicType = metadataAccessor.getType(clazz);
        var callerId = configuration.getCallerId();
        var publishers =
                getMasterApi()
                        .registerSubscriber(
                                callerId, topic, topicType, configuration.getNodeApiUrl());
        LOGGER.log(Level.FINE, "Publishers: {0}", publishers.toString());
        if (publishers.value.isEmpty()) {
            throw new XRE("No publishers for topic %s found", topic);
        }

        // closed once subscriber cancels subscription
        @SuppressWarnings("resource")
        var processor = new MergeProcessor<M>();
        processor.subscribe(subscriber);
        for (var publisher : publishers.value /* .stream().collect(Collectors.toSet()) */) {
            try {
                LOGGER.log(Level.FINE, "Registering with publisher: {0}", publisher);
                var nodeApi = getNodeApi(publisher);
                var protocol = nodeApi.requestTopic(callerId, topic, List.of(Protocol.TCPROS));
                LOGGER.log(Level.FINE, "Protocol configuration: {0}", protocol);
                var nodeClient =
                        new TcpRosClient<M>(
                                callerId, topic, protocol.host, protocol.port, clazz, textUtils);
                nodeClient.subscribe(processor.newSubscriber());
                nodeClient.connect();
                clients.add(nodeClient);
            } catch (Exception e) {
                LOGGER.log(Level.FINE, "Failed to register with publisher: {0}", e.getMessage());
            }
        }
    }

    /**
     * Create a new topic and start publishing messages for it.
     *
     * @param <M> type of messages in the topic
     * @param publisher provides information about new topic. Once topic created publisher is used
     *     to emit messages which will be sent to topic subscribers
     */
    public <M extends Message> void publish(TopicPublisher<M> publisher) throws Exception {
        var topic = publisher.getTopic();
        var clazz = publisher.getMessageClass();
        var topicType = metadataAccessor.getType(clazz);
        publishersManager.add(publisher);
        tcpRosServer.start();
        nodeServer.start();
        var subscribers =
                getMasterApi()
                        .registerPublisher(
                                configuration.getCallerId(),
                                topic,
                                topicType,
                                configuration.getNodeApiUrl());
        LOGGER.log(Level.FINE, "Current subscribers: {0}", subscribers.toString());
    }

    /**
     * Unregister publisher in Master node and stop publisher from emitting new messages
     *
     * @param topic name of the topic used by the publisher
     */
    public void unpublish(String topic) throws IOException {
        var publisherOpt = publishersManager.getPublisher(utils.formatTopicName(topic));
        if (publisherOpt.isEmpty()) {
            LOGGER.log(
                    Level.FINE,
                    "There is no publishers for topic {0}, nothing to unpublish",
                    topic);
            return;
        }
        var publisher = publisherOpt.get();
        publisher.close();
        try {
            var num =
                    getMasterApi()
                            .unregisterPublisher(
                                    configuration.getCallerId(),
                                    topic,
                                    configuration.getNodeApiUrl());
            LOGGER.log(Level.FINE, "Unregistered publisher response: {0}", num.toString());
        } finally {
            publishersManager.remove(topic);
        }
    }

    /** Check if there is any publisher available for the given topic */
    public boolean hasPublisher(String topic) {
        return getMasterApi().getSystemState(configuration.getCallerId()).publishers.stream()
                .anyMatch(p -> topic.equals(p.topic));
    }

    /** Release the resources, stop TCPROS server and node server */
    @Override
    public void close() throws IOException {
        try {
            var exception = new RuntimeException();
            publishersManager.getPublishers().stream()
                    .map(TopicPublisher::getTopic)
                    .forEach(Unchecked.wrapAccept(this::unpublish, exception));
            if (exception.getSuppressed().length != 0) throw exception;
        } finally {
            nodeServer.close();
            clients.forEach(Unchecked.wrapAccept(TcpRosClient::close));
            tcpRosServer.close();
            clients.clear();
        }
    }
}
