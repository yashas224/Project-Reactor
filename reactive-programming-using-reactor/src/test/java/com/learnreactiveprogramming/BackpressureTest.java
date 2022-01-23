package com.learnreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BackpressureTest {

    @Test
    void testBackPresure() {
        var numberrange = Flux.range(1, 100).log();

        numberrange
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("hookOnNext {}", value);
                        if (value == 2) {
                            cancel();
                        }
                    }

                    @Override
                    protected void hookOnCancel() {
                        log.info("Indide hookOnCancel ");
                    }
                });
    }

    @Test
    void testBackPresure_1() throws InterruptedException {
        var numberrange = Flux.range(1, 100).log();
        CountDownLatch latch = new CountDownLatch(1);
        numberrange
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("hookOnNext {}", value);
                        if (value % 2 == 0 || value < 50) {
                            request(2);
                        } else if (value == 51) {
                            cancel();
                        }
                    }

                    @Override
                    protected void hookOnCancel() {
                        log.info("Indide hookOnCancel ");
                        latch.countDown();
                    }
                });

        Assertions.assertTrue(latch.await(5, TimeUnit.MILLISECONDS));
    }

    @Test
    void testBackPresure_drop() throws InterruptedException {
        var numberrange = Flux.range(1, 100).log();
        CountDownLatch latch = new CountDownLatch(1);
        numberrange
                .onBackpressureDrop(item -> {
                    log.info("dropped item are {}", item);
                })
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
//                        log.info("hookOnNext {}", value);
//                        if (value % 2 == 0 || value < 50) {
//                            request(2);
//                        } else if (value == 51) {
//                            cancel();
//                        }

                        if (value == 2) {
                            hookOnCancel();
                        }
                    }

                    @Override
                    protected void hookOnCancel() {
                        log.info("Indide hookOnCancel ");
                        latch.countDown();
                    }
                });

        Assertions.assertTrue(latch.await(5, TimeUnit.MILLISECONDS));
    }


    @Test
    void testBackPresure_buffer() throws InterruptedException {
        var numberrange = Flux.range(1, 100).log();
        CountDownLatch latch = new CountDownLatch(1);
        numberrange
                .onBackpressureBuffer(10, i -> {
                    log.info("last buffer element is {}", i);
                })
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("hookOnNext {}", value);
                        if (value < 50) {
                            request(1);
                        } else {
                            hookOnCancel();
                        }
                    }

                    @Override
                    protected void hookOnCancel() {
                        log.info("Indide hookOnCancel ");
                        latch.countDown();
                    }
                });

        Assertions.assertTrue(latch.await(5, TimeUnit.MILLISECONDS));
    }

    @Test
    void testBackPresure_error() throws InterruptedException {
        var numberrange = Flux.range(1, 100).log();
        CountDownLatch latch = new CountDownLatch(1);
        numberrange
                .onBackpressureError()
//                .doOnError(error -> {
//                    log.info("Exception occured {}", error);
//                })
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("hookOnNext {}", value);
                        if (value < 50) {
                            request(1);
                        } else {
                            hookOnCancel();
                        }
                    }

                    @Override
                    protected void hookOnCancel() {
                        log.info("Indide hookOnCancel ");
                        latch.countDown();
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        log.info("Exception occured {}", throwable);

                    }
                });

        Assertions.assertTrue(latch.await(5, TimeUnit.MILLISECONDS));
    }
}
