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
package id.jrosclient.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import id.jrosclient.impl.MergeProcessor;

public class MergeProcessorTest {

    @ParameterizedTest
    @CsvSource({
        "1, 10, false",
        "1, 10, true",
        "2, 10, false",
        "60, 500, false"
    })
    public void test(int numOfPublishers, int numOfItems, boolean keepRequesting) throws InterruptedException, ExecutionException {
        var future = new CompletableFuture<Void>();
        var proc = new MergeProcessor<Integer>();
        var subscriber = new TestMultiSubscriber(numOfPublishers, numOfItems)
                .withFuture(future);
        if (keepRequesting) subscriber.withKeepRequesting();
        proc.subscribe(subscriber);
        var executor = Executors.newCachedThreadPool();
        IntStream.range(0, numOfPublishers)
            .forEach(i -> runPublishLoop(executor, i, proc));
        future.get();
        executor.shutdown();
        boolean ret = executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        assertEquals(true, ret);
        assertEquals(0, subscriber.getOnErrorCounter());
        if (keepRequesting) {
            while (subscriber.getOnCompleteCounter() != 1);
        } else {
            assertEquals(0, subscriber.getOnCompleteCounter());
        }
        while (!proc.isClosed());
    }

    /**
     * Run publishing loop which will constantly publish items to the processor
     */
    private void runPublishLoop(ExecutorService executor, int item, MergeProcessor<Integer> proc) {
        executor.submit(() -> {
            try (var publisher = new SubmissionPublisher<Integer>()) {
                publisher.subscribe(proc.newSubscriber());
                while (!executor.isShutdown()) {
                    //System.out.println("Submitting to publisher item " + item);
                    publisher.submit(item);
                }
            }
            System.out.println("Closed publisher of item " + item);
        });        
    }
}
