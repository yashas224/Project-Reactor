package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

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
                .expectNext("alex")
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
                .expectNext("a", "l", "e", "x", "c", "h", "l", "o", "e")
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
}
