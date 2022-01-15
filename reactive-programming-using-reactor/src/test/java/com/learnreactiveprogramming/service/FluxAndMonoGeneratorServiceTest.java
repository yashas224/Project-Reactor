package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

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
        var nameMono=fluxAndMonoSchedulersService.nameMono();
        StepVerifier.create(nameMono)
                .expectNext("alex")
                .verifyComplete();
    }
}
