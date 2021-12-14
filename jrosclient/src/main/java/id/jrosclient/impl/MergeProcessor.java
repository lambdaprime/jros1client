/*
 * Copyright 2021 jrosclient project
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
package id.jrosclient.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.atomic.AtomicInteger;

import id.xfunction.logging.XLogger;

/**
 * Merge multiple subscriptions for different publishers
 * into one.
 * 
 * <p>This processor can be subscribed to many publishers. It will
 * manage all the subscriptions from them and will deliver all messages
 * it receives to its own subscriber. It supports only one subscriber at a time.
 * 
 * <p>This processor stops only when there is no more active publishers
 * (all of them either issued {@link #onComplete()} or {@link #onError(Throwable)})
 * and it:
 * 
 * <ul>
 * <li>issues {@link #onComplete()} only when all publishers
 * to which this processor subscribed issued {@link #onComplete()}.
 * <li>if at least one of the publishers issued {@link #onError(Throwable)} then this
 * processor will issue {@link #onError(Throwable)} instead of {@link #onComplete()}
 * with all the exceptions suppressed inside
 * </ul>
 *
 */
public class MergeProcessor<T> extends SubmissionPublisher<T> {
    private static final XLogger LOGGER = XLogger.getLogger(MergeProcessor.class);

    private AtomicInteger numOfActiveSubscriptions = new AtomicInteger();
    private List<Throwable> failed = new CopyOnWriteArrayList<>();
    
    public Subscriber<T> newSubscriber() {
        return new Subscriber<T>() {
            private Subscription subscription;
            private int id = hashCode();

            @Override
            public void onSubscribe(Subscription subscription) {
                LOGGER.entering("onSubscribe", id);
                this.subscription = subscription;
                numOfActiveSubscriptions.incrementAndGet();
                this.subscription.request(1);
            }

            @Override
            public void onNext(T item) {
                LOGGER.entering("onNext", id);
                if (noSubscribers()) return;
                submit(item);
                subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                LOGGER.entering("onError", id);
                if (noSubscribers()) return;
                failed.add(throwable);
                if (numOfActiveSubscriptions.decrementAndGet() == 0) {
                    closeExceptionally(aggregateExceptions());
                }
            }

            @Override
            public void onComplete() {
                LOGGER.entering("onComplete", id);
                if (noSubscribers()) return;
                if (numOfActiveSubscriptions.decrementAndGet() == 0) {
                    if (failed.isEmpty()) {
                        close();
                    } else {
                        closeExceptionally(aggregateExceptions());
                    }
                }
            }
            
            private boolean noSubscribers() {
                if (getNumberOfSubscribers() != 0) return false;
                LOGGER.fine("{0}: no more subscribers, canceling subscription and closing processor", id);
                subscription.cancel();
                close();
                return true;
            }

        };
    }

    public int getNumOfActiveSubscriptions() {
        return numOfActiveSubscriptions.get();
    }
    
    private Exception aggregateExceptions() {
        var exception = new Exception("Some of the publishers terminated with errors (see suppressed exceptions)");
        failed.forEach(exception::addSuppressed);
        return exception;
    }

}
