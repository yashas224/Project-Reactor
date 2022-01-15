package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

public class FluxAndMonoSchedulersService {

    static List<String> namesList = List.of("alex", "ben", "chloe");

    private String upperCase(String name) {
        delay(1000);
        return name.toUpperCase();
    }

    // in reality flux might be coming from a Db of remote service Call
    // Flux is a Publisher as it extends Publisher interface
    public Flux<String> namesFlux() {
        return Flux.fromIterable(namesList).log();
    }

    public Mono<String> nameMono() {
        return Mono.just("yashas").log();
    }

    public static void main(String[] args) {
        FluxAndMonoSchedulersService fluxAndMonoSchedulersService = new FluxAndMonoSchedulersService();
        fluxAndMonoSchedulersService.namesFlux()
                .subscribe(n -> System.out.println(n));


        System.out.println("Mono Displaying");

        fluxAndMonoSchedulersService.nameMono()
                .subscribe(System.out::println);

    }
}
