package id.jrosclient.tests;

import static java.util.function.Function.identity;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Subscriber which can be subscribed to multiple publishers and test that
 * each of them publishes certain number of items. 
 */
public class TestMultiSubscriber implements Subscriber<Integer> {
    
    private Map<Integer, Integer> counters;
    private int onErrorCounter;
    private int onCompleteCounter;
    private Subscription subscription;
    private boolean hasKeepRequesting;
    private CompletableFuture<Void> future;

    /**
     * @param numOfPublishers number of publishers which need to expect
     * @param numOfItems number of items to expect from each publisher
     */
    public TestMultiSubscriber(int numOfPublishers, int numOfItems) {
        counters = IntStream.range(0, numOfPublishers)
                .boxed()
                .collect(Collectors.toMap(identity(), k -> numOfItems));
        System.out.println("Counters " + counters);
    }
    
    /**
     * By default subscriber cancels the subscription when it receives
     * all items from all publishers. With this flag it will keep
     * continue requesting items forever. 
     */
    public TestMultiSubscriber withKeepRequesting() {
        hasKeepRequesting = true;
        return this;
    }
    
    /**
     * Set future which will be completed once subscriber receives all items
     * from all publishers
     */
    public TestMultiSubscriber withFuture(CompletableFuture<Void> future) {
        this.future = future;
        return this;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(Integer item) {
        var c = counters.getOrDefault(item, 0);
        if (c == 0) {
            counters.remove(item);
            if (counters.isEmpty()) {
                future.complete(null);
                if (!hasKeepRequesting) {
                    System.out.println("Terminating subscriber");
                    subscription.cancel();
                    return;
                }
            }
        } else {
            counters.put(item, c - 1);
        }
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        onErrorCounter++;
    }

    @Override
    public void onComplete() {
        onCompleteCounter++;
    }

    public int getOnCompleteCounter() {
        return onCompleteCounter;
    }
    
    public int getOnErrorCounter() {
        return onErrorCounter;
    }
}
