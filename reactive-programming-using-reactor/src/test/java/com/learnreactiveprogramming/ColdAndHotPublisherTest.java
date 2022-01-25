package com.learnreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
public class ColdAndHotPublisherTest {
    // prrof that subscribe returns after its invocation
    @Test
    void asyncTest() throws InterruptedException {
        log.info("Test  Thread  isDaemon {}", Thread.currentThread().isDaemon());
        var flux1 = Flux.range(1, 10).delayElements(Duration.ofMillis(1000)).log();
        var flux = Flux.range(1, 10).delayElements(Duration.ofMillis(2000)).log();
        flux.subscribe((val) -> {
            log.info("Thread isDaemon {}", Thread.currentThread().isDaemon());
            log.info("subscriber 1: " + val);
        });
        flux1.subscribe((val) -> log.info("subscriber 2: " + val));

        for (int i = 0; i < 10; i++) {
            Thread.sleep(800);
            log.info("Other work here in {} Thread ", Thread.currentThread().getName());
        }

        Thread.sleep(20000);

    }

    @Test
    void coldPublisherTest() throws InterruptedException {
        var flux = Flux.range(1, 10).delayElements(Duration.ofMillis(1000));
        flux.subscribe((val) -> {
            log.info("subscriber 1: " + val);
        });

        Thread.sleep(5000);
        flux.subscribe((val) -> {
            log.info("subscriber 2: " + val);
        });

        Thread.sleep(15000);
    }

    @Test
    void hotPublisherTest() throws InterruptedException {
        var flux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(1000));
        ConnectableFlux<Integer> connectableFlux = flux.publish();
        connectableFlux.connect();

        connectableFlux.subscribe((val) -> System.out.println("subscriber 1: " + val));
        Thread.sleep(4000);
        connectableFlux.subscribe((val) -> System.out.println("subscriber 2: " + val));


        Thread.sleep(100000);
    }

    // connect() starts streaming data without waiting for subscriptions
    @Test
    void hotPublisherTest_1() throws InterruptedException {
        var flux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(1000));
        ConnectableFlux<Integer> connectableFlux = flux.publish();
        connectableFlux.connect();

        Thread.sleep(5000);
        connectableFlux.subscribe((val) -> System.out.println("subscriber 2: " + val));


        Thread.sleep(100000);
    }


    @Test
    void hotPublisherTest_AutoConnect() throws InterruptedException {
        var flux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(1000));
        Flux<Integer> hotSource = flux.publish().autoConnect(2);

        hotSource.subscribe((val) -> System.out.println("subscriber 1: " + val));
        log.info("first Subscribers is connected");
        Thread.sleep(6000);
        hotSource.subscribe((val) -> System.out.println("subscriber 2: " + val));
        log.info("two Subscribers are connected");
        Thread.sleep(4000);
        hotSource.subscribe((val) -> System.out.println("subscriber 3: " + val));

        Thread.sleep(100000);
    }

    // if cancelled the stream resumes from its stop after any subscribers have  subscribed
    @Test
    void hotPublisherTest_AutoConnect_1() throws InterruptedException {
        var flux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(1000));
        Flux<Integer> hotSource = flux.publish().autoConnect(2);

        var disposale = hotSource.subscribe((val) -> System.out.println("subscriber 1: " + val));
        log.info("first Subscribers is connected");
        Thread.sleep(6000);
        var disposale1 = hotSource.subscribe((val) -> System.out.println("subscriber 2: " + val));
        log.info("two Subscribers are connected");
        Thread.sleep(4000);
        disposale.dispose();
        disposale1.dispose();
        Thread.sleep(5000);
        hotSource.subscribe((val) -> System.out.println("subscriber 3: " + val));

        Thread.sleep(10000);
    }


    // is all minium subscriptions are cancelled, restarts if and only if minium Subscribers have subscribed again and restarts the stream.
    // Does not restart otherwise.

    @Test
    void hotPublisherTest_refCount_1() throws InterruptedException {
        var flux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(1000)).doOnCancel(() -> log.info("received cancall signal"));
        Flux<Integer> hotSource = flux.publish().refCount(2);

        var disposale = hotSource.subscribe((val) -> System.out.println("subscriber 1: " + val));
        log.info("first Subscribers is connected");
        Thread.sleep(6000);
        var disposale1 = hotSource.subscribe((val) -> System.out.println("subscriber 2: " + val));
        log.info("two Subscribers are connected");
        Thread.sleep(4000);
        disposale.dispose();
        disposale1.dispose();
        Thread.sleep(5000);
        hotSource.subscribe((val) -> System.out.println("subscriber 3: " + val));
        Thread.sleep(1000);
        hotSource.subscribe((val) -> System.out.println("subscriber 4: " + val));

        Thread.sleep(10000);
    }
}
