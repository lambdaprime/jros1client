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
package id.jrosclient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import id.jrosclient.impl.RosRpcClient;
import id.jrosclient.impl.TextUtils;
import id.jrosclient.impl.TextUtilsFactory;
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
import id.jrosmessages.impl.MetadataAccessor;
import id.xfunction.XRE;
import id.xfunction.function.Unchecked;
import id.xfunction.logging.XLogger;

/**
 * Main class of the library which allows to interact with ROS.
 */
public class JRosClient implements AutoCloseable {

    private static final Logger LOGGER = XLogger.getLogger(JRosClient.class);

    private String masterUrl;
    private NodeServer nodeServer;
    private TcpRosServer tcpRosServer;
    private MetadataAccessor metadataAccessor = new MetadataAccessor();
    private Set<TcpRosClient<?>> clients = new HashSet<>();
    private PublishersManager publishersManager = new PublishersManager();
    private JRosClientConfiguration configuration;
    private TextUtils utils;

    /**
     * @param masterUrl master node URL
     */
    public JRosClient(String masterUrl) {
        this(masterUrl, new JRosClientConfiguration());
    }

    public JRosClient(String masterUrl, JRosClientConfiguration config) {
        this.masterUrl = masterUrl;
        nodeServer = new NodeServer(config);
        tcpRosServer = new TcpRosServer(publishersManager, config);
        configuration = config;
        utils = TextUtilsFactory.create(config);
    }

    /**
     * Access to ROS master API
     */
    public MasterApi getMasterApi() {
        RosRpcClient client = new RosRpcClient(masterUrl);
        return new MasterApiClientImpl(client);
    }

    /**
     * Return Node API of the foreign node. It allows to
     * interact with other ROS nodes directly.
     * @param nodeUrl URL of the foreign node to connect
     */
    public NodeApi getNodeApi(String nodeUrl) {
        RosRpcClient client = new RosRpcClient(nodeUrl);
        return new NodeApiClientImpl(client);
    }

    /**
     * Subscribe to ROS topic
     * @param <M> type of messages in the topic
     * @param subscriber provides information about the topic to subscribe
     * for. Once subscribed it will be notified for
     * any new message which gets published with given topic.
     * @throws Exception
     */
    public <M extends Message> void subscribe(TopicSubscriber<M> subscriber) 
            throws Exception
    {
        String topic = subscriber.getTopic();
        var clazz = subscriber.getMessageClass();
        var topicType = metadataAccessor.getType(clazz);
        var callerId = configuration.getCallerId();
        var publishers = getMasterApi().registerSubscriber(callerId, topic, topicType,
                configuration.getNodeApiUrl());
        LOGGER.log(Level.FINE, "Publishers: {0}", publishers.toString());
        if (publishers.value.isEmpty()) {
            throw new XRE("No publishers for topic %s found", topic);
        }
        var nodeApi = getNodeApi(publishers.value.get(0));
        var protocol = nodeApi.requestTopic(callerId, topic, List.of(Protocol.TCPROS));
        LOGGER.log(Level.FINE, "Protocol configuration: {0}", protocol);
        var nodeClient = new TcpRosClient<M>(callerId, topic, protocol.host, protocol.port, clazz, utils);
        nodeClient.subscribe(subscriber);
        nodeClient.connect();
        clients.add(nodeClient);
    }

    /**
     * Create a new topic and start publishing messages for it.
     * @param <M> type of messages in the topic
     * @param publisher provides information about new topic. Once topic
     * created publisher is used to emit messages which will be sent
     * to topic subscribers
     * @throws Exception
     */
    public <M extends Message> void publish(TopicPublisher<M> publisher) 
            throws Exception
    {
        var topic = publisher.getTopic();
        var clazz = publisher.getMessageClass();
        var topicType = metadataAccessor.getType(clazz);
        publishersManager.add(publisher);
        tcpRosServer.start();
        nodeServer.start();
        var subscribers = getMasterApi().registerPublisher(configuration.getCallerId(), topic, topicType,
                configuration.getNodeApiUrl());
        LOGGER.log(Level.FINE, "Current subscribers: {0}", subscribers.toString());
    }

    /**
     * Unregister publisher in Master node and stop publisher from emitting
     * new messages
     * @param topic name of the topic used by the publisher
     * @throws Exception
     */
    public void unpublish(String topic) 
            throws Exception
    {
        var publisherOpt = publishersManager.getPublisher(topic);
        if (publisherOpt.isEmpty()) {
            LOGGER.log(Level.FINE, "There is no publishers for topic {0}, nothing to unpublish",
                    topic);
            return;
        }
        var publisher = publisherOpt.get();
        publisher.close();
        var num = getMasterApi().unregisterPublisher(configuration.getCallerId(), topic,
                configuration.getNodeApiUrl());
        LOGGER.log(Level.FINE, "Unregistered publisher response: {0}", num.toString());
        publishersManager.remove(topic);
    }
    
    /**
     * Check if there is any publisher available for the given topic
     */
    public boolean hasPublisher(String topic) {
        return getMasterApi().getSystemState(configuration.getCallerId()).publishers.stream()
                .anyMatch(p -> topic.equals(p.topic));
    }

    /**
     * Release the resources, stop TCPROS server and node server  
     */
    @Override
    public void close() throws Exception {
        nodeServer.close();
        clients.forEach(Unchecked.wrapAccept(TcpRosClient::close));
        tcpRosServer.close();
        clients.clear();
    }

}
