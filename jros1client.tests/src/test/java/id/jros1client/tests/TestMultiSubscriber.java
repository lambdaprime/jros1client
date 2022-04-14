/*
 * Copyright 2022 jrosclient project
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
package id.jros1client.tests;

import static java.util.function.Function.identity;

import id.xfunction.logging.XLogger;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Subscriber which can be subscribed to multiple publishers and test that each of them publishes
 * certain number of items.
 *
 * @author lambdaprime intid@protonmail.com
 */
public class TestMultiSubscriber implements Subscriber<Integer> {
    private static final XLogger LOGGER = XLogger.getLogger(TestMultiSubscriber.class);
    private Map<Integer, Integer> counters;
    private volatile int onErrorCounter;
    private volatile int onCompleteCounter;
    private Subscription subscription;
    private boolean hasKeepRequesting;
    private CompletableFuture<Void> future;

    /**
     * @param numOfPublishers number of publishers which need to expect
     * @param numOfItems number of items to expect from each publisher
     */
    public TestMultiSubscriber(int numOfPublishers, int numOfItems) {
        counters =
                IntStream.range(0, numOfPublishers)
                        .boxed()
                        .collect(Collectors.toConcurrentMap(identity(), k -> numOfItems));
        LOGGER.info("Counters " + counters);
    }

    /**
     * By default subscriber cancels the subscription when it receives all items from all
     * publishers. With this flag it will keep continue requesting items forever.
     */
    public TestMultiSubscriber withKeepRequesting() {
        hasKeepRequesting = true;
        return this;
    }

    /** Set future which will be completed once subscriber receives all items from all publishers */
    public TestMultiSubscriber withFuture(CompletableFuture<Void> future) {
        this.future = future;
        return this;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        LOGGER.entering("onSubscribe");
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(Integer item) {
        LOGGER.entering("onNext", item.toString());
        var c = counters.getOrDefault(item, 0);
        if (c == 0) {
            counters.remove(item);
            if (counters.isEmpty()) {
                future.complete(null);
                if (!hasKeepRequesting) {
                    LOGGER.fine("Terminating subscriber");
                    subscription.cancel();
                    LOGGER.exiting("onNext");
                    return;
                }
            }
        } else {
            counters.put(item, c - 1);
        }
        subscription.request(1);
        LOGGER.exiting("onNext");
    }

    @Override
    public void onError(Throwable throwable) {
        LOGGER.entering("onError", throwable);
        onErrorCounter++;
    }

    @Override
    public void onComplete() {
        LOGGER.entering("onComplete");
        onCompleteCounter++;
    }

    public int getOnCompleteCounter() {
        return onCompleteCounter;
    }

    public int getOnErrorCounter() {
        return onErrorCounter;
    }
}
