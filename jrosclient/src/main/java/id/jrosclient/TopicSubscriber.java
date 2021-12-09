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
package id.jrosclient;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;

import id.jrosclient.impl.Utils;
import id.jrosmessages.Message;

/**
 * <p>Subscriber receives messages for the topic it is
 * subscribed for and passes them to the onNext method.</p>
 * 
 * <p>This class simplifies implementation of new subscribers since it
 * provides default implementations for most methods except onNext.
 * The onNext method needs to be implemented for each subscriber
 * separately.</p>
 * 
 * <p>Before subscriber can start to receive messages it needs
 * to be subscribed for the topic.</p>
 * 
 * <p>See <a href="{@docRoot}/../index.html">Module documentation</a> for examples.</p>
 * 
 * @param <M> type of messages in the topic
 */
public abstract class TopicSubscriber<M extends Message> implements Flow.Subscriber<M> {

    private static final Utils utils = new Utils();

    private Class<M> messageClass;
    private Subscription subscription;
    private String topic;
    
    /**
     * @param messageClass class of the messages in this topic
     * @param topic Name of the topic which messages current subscriber wants to receive.
     * Topic name which should start from '/'
     */
    public TopicSubscriber(Class<M> messageClass, String topic) {
        this.messageClass = messageClass;
        this.topic = utils.formatTopicName(topic);
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    /**
     * <p>Common throwable types:</p>
     * 
     * <ul>
     * <li>EOFException - publisher unexpectedly closed the connection</li>
     * </ul>
     */
    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    /**
     * Default implementation does nothing but it can be redefined.
     */
    @Override
    public void onComplete() {
        
    }

    /**
     * Class of the messages in the current topic
     */
    public Class<M> getMessageClass() {
        return messageClass;
    }

    public Subscription getSubscription() {
        return subscription;
    }
    
    /**
     * Name of the topic of this subscriber
     */
    public String getTopic() {
        return topic;
    }
}
