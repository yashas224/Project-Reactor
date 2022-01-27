package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;
import reactor.util.function.Tuple4;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;


public class FluxAndMonoGeneratorServiceTest {
    FluxAndMonoSchedulersService fluxAndMonoSchedulersService = new FluxAndMonoSchedulersService();

    @Test
    void namesFlux() {

        var namesFlux = fluxAndMonoSchedulersService.namesFlux();

        StepVerifier.create(namesFlux)
//                .expectNext("alex", "ben", "chloe")
//                .expectNextCount(3)
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();

    }

    @Test
    void nameMono() {
        var nameMono = fluxAndMonoSchedulersService.nameMono();
        StepVerifier.create(nameMono)
                .expectNext("yashas")
                .verifyComplete();
    }


    @Test
    void namesFlux_map() {
        var namesFlux_map = fluxAndMonoSchedulersService.namesFlux_map();
        StepVerifier.create(namesFlux_map)
                .expectNext("ALEX", "BEN", "CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFlux_immutability() {
        var namesFlux_map = fluxAndMonoSchedulersService.namesFlux_immutability();
        StepVerifier.create(namesFlux_map)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    void namesFlux_filter() {
        var namesFlux = fluxAndMonoSchedulersService.namesFlux_filter(3);
        StepVerifier.create(namesFlux)
                .expectNext("4-ALEX", "5-CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatMap() {
        var namesFlux = fluxAndMonoSchedulersService.namesFlux_flatMap(3);

        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatMapAsync() {
        var flux = fluxAndMonoSchedulersService.namesFlux_flatMapAsync(3);

        StepVerifier.create(flux)
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_concatMap() {
        var flux = fluxAndMonoSchedulersService.namesFlux_concatMap(3);
        StepVerifier.create(flux)
                .expectNext("a", "l", "e", "x", "c", "h", "l", "o", "e")
                .verifyComplete();
    }

    @Test
    void namesFlux_concatMap_virtualTimer() {
        VirtualTimeScheduler.getOrSet();
        var flux = fluxAndMonoSchedulersService.namesFlux_concatMap(3);
        StepVerifier.withVirtualTime(() -> flux)
                .thenAwait(Duration.ofSeconds(9))
                .expectNext("a", "l", "e", "x", "c", "h", "l", "o", "e")
                .verifyComplete();
    }

    @Test
    void nameMono_flatmap() {
        var mono = fluxAndMonoSchedulersService.nameMono_flatmap();
        StepVerifier.create(mono)
                .expectNext(List.of("y", "a", "s", "h", "a", "s"))
                .verifyComplete();
    }

    @Test
    void nameMono_flatmapMany() {
        var mono = fluxAndMonoSchedulersService.nameMono_flatmapMany();
        StepVerifier.create(mono)
                .expectNext("y", "a", "s", "h", "a", "s")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform() {
        var flux = fluxAndMonoSchedulersService.namesFlux_transform(3);
        StepVerifier.create(flux)
                .expectNext("ALEX", "CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_1() {
        var flux = fluxAndMonoSchedulersService.namesFlux_transform(6);
        StepVerifier.create(flux)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_switchifempty() {
        var flux = fluxAndMonoSchedulersService.namesFlux_transform_switchifempty(8);
        StepVerifier.create(flux)
                .expectNext("x", "x", "x")
                .verifyComplete();

    }

    @Test
    void explore_concat() {
        var concatedFlux = fluxAndMonoSchedulersService.explore_concat();
        StepVerifier.create(concatedFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_concatwith() {
        var concatedFlux = fluxAndMonoSchedulersService.explore_concatwith();
        StepVerifier.create(concatedFlux)
                .expectNext("A", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_merge() {
        var mergedFlux = fluxAndMonoSchedulersService.explore_merge();
        StepVerifier.create(mergedFlux)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    void explore_mergewith() {
        var mergedFlux = fluxAndMonoSchedulersService.explore_mergewith();
        StepVerifier.create(mergedFlux)
                .expectNext("A", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_merge_sequestial() {
        var mergedFlux = fluxAndMonoSchedulersService.explore_merge_sequestial();
        StepVerifier.create(mergedFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_zip() {
        var zippedFlux = fluxAndMonoSchedulersService.explore_zip();
        StepVerifier.create(zippedFlux)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void explore_zip_1() {
        var tupelFlux = fluxAndMonoSchedulersService.explore_zip_1();
        StepVerifier.create(tupelFlux)
                .expectNext("AD14", "BE25", "CF36")
                .verifyComplete();
    }

    @Test
    void explore_zipwith() {
        var zipedFlux = fluxAndMonoSchedulersService.explore_zipwith();
        StepVerifier.create(zipedFlux)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void explore_zipwith_mono() {
        var monoZipped = fluxAndMonoSchedulersService.explore_zipwith_mono();
        StepVerifier.create(monoZipped)
                .expectNext("AD")
                .verifyComplete();
    }

    @Test
    void namesFlux_do_on_callback() {
        var flux = fluxAndMonoSchedulersService.namesFlux_do_on_callback();
        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void exceptionFlux() {
        var flux = fluxAndMonoSchedulersService.exceptionFlux();
        StepVerifier.create(flux)
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    //  second way to test exceptions
    @Test
    void exceptionFlux_1() {
        var flux = fluxAndMonoSchedulersService.exceptionFlux();
        StepVerifier.create(flux)
                .expectNext("A", "B", "C")
                .expectError()
                .verify();
    }

    //  third way to test exceptions
    @Test
    void exceptionFlux_2() {
        var flux = fluxAndMonoSchedulersService.exceptionFlux();
        StepVerifier.create(flux)
                .expectNext("A", "B", "C")
                .expectErrorMessage("EXCEPTION OCCURED")
                .verify();
    }

    @Test
    void explore_onErrorReturn() {
        var flux = fluxAndMonoSchedulersService.explore_onErrorReturn();

        StepVerifier.create(flux)
                .expectNext("A", "B", "C")
                .expectNext("E")
                .verifyComplete();
    }

    @Test
    void explore_onErrorResume() {
        var flux = fluxAndMonoSchedulersService.explore_onErrorResume(new IllegalStateException("Not a valid state"));
        StepVerifier.create(flux)
                .expectNext("A", "B", "C")
                .expectNext("X", "Y", "Z")
                .verifyComplete();
    }

    @Test
    void explore_onErrorResume_1() {
        var flux = fluxAndMonoSchedulersService.explore_onErrorResume(new RuntimeException("Not a valid state"));
        StepVerifier.create(flux)
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void explore_onErrorContinue() {
        var flux = fluxAndMonoSchedulersService.explore_onErrorContinue();
        StepVerifier.create(flux)
                .expectNext("A", "C", "D")
                .verifyComplete();
    }

    @Test
    void explore_onErrorMap() {
        var flux = fluxAndMonoSchedulersService.explore_onErrorMap();
        StepVerifier.create(flux)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void explore_doOnError() {
        var flux = fluxAndMonoSchedulersService.explore_doOnError();
        StepVerifier.create(flux)
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void explore_Mono_OnErrorReturn() {
        var mono = fluxAndMonoSchedulersService.explore_Mono_OnErrorReturn();
        StepVerifier.create(mono)
                .expectNext("D")
                .verifyComplete();
    }

    @Test
    void explore_publishOn() {
        var flux = fluxAndMonoSchedulersService.explore_publishOn();

        StepVerifier.create(flux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void explore_subscribeOn() {
        var flux = fluxAndMonoSchedulersService.explore_subscribeOn();

        StepVerifier.create(flux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void explore_parallel() {
        var flux = fluxAndMonoSchedulersService.explore_parallel();
        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void explore_parallel_usingFlatmap() {
        var flux = fluxAndMonoSchedulersService.explore_parallel_usingFlatmap();
        StepVerifier.create(flux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void explore_parallel_usingFlatmap_1() {
        var flux = fluxAndMonoSchedulersService.explore_parallel_usingFlatmap_1();
        StepVerifier.create(flux)
                .expectNextCount(6)
                .verifyComplete();

    }


    @Test
    void explore_merge2() {
        var flux = fluxAndMonoSchedulersService.explore_merge_without_thread_switching();
        StepVerifier.create(flux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void explore_parallel_usingFlatmapSequential() {
        var flux = fluxAndMonoSchedulersService.explore_parallel_usingFlatmapSequential();
        StepVerifier.create(flux)
                .expectNext("ALEX", "BEN", "CHLOE")
                .verifyComplete();
    }

    @Test
    void explore_generate() {
        var flux = fluxAndMonoSchedulersService.explore_generate().log();
        StepVerifier.create(flux)
                .expectNext(2, 4, 6, 8, 10, 12, 14, 16, 18, 20)
                .verifyComplete();
    }

    @Test
    void explorre_create() {
        var flux = fluxAndMonoSchedulersService.explorre_create().log();
        StepVerifier.create(flux)
                .expectNext("alex", "alex", "ben", "ben", "chloe", "chloe", "alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    void explorre_create_mono() {
        var mono = fluxAndMonoSchedulersService.explorre_create_mono().log();
        StepVerifier.create(mono)
                .expectNext("yashas")
                .verifyComplete();
    }

    @Test
    void explore_handle() {
        var flux = fluxAndMonoSchedulersService.explore_handle().log();
        StepVerifier.create(flux)
                .expectNext("ALEX", "CHLOE")
                .verifyComplete();
    }
}

