package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

    public Flux<String> namesFlux_map() {
        return Flux.fromIterable(namesList)
                .map(String::toUpperCase).log();
    }

    public Flux<String> namesFlux_immutability() {
        var namesFlux = Flux.fromIterable(namesList);
        namesFlux.map(String::toUpperCase).log();
        return namesFlux;
    }


    public Flux<String> namesFlux_filter(int strLength) {
        return Flux.fromIterable(namesList)
                .map(String::toUpperCase)
                .filter(s -> s.length() > strLength)
                .map(s -> s.length() + "-" + s)
                .log();
    }

    // return individual Characters of each name
    public Flux<String> namesFlux_flatMap(int strLength) {
        return Flux.fromIterable(namesList)
                .filter(s -> s.length() > strLength)
                .flatMap(s -> splitStringToFlux(s)).log();
    }

    public Flux<String> splitStringToFlux(String s) {
        return Flux.fromArray(s.split(""));
    }

    public Flux<String> namesFlux_flatMapAsync(int strLength) {
        return Flux.fromIterable(namesList)
                .filter(s -> s.length() > strLength)
                .flatMap(s -> splitStringToFluxWithDelay(s)).log();
    }

    public Flux<String> splitStringToFluxWithDelay(String s) {
        return Flux.fromArray(s.split(""))
                .delayElements(Duration.ofMillis(new Random().nextInt(1000)));
    }

    public Flux<String> namesFlux_concatMap(int strLength) {
        return Flux.fromIterable(namesList)
                .filter(s -> s.length() > strLength)
                .concatMap(s -> splitStringToFluxWithDelay(s)).log();
    }

    public Mono<String> nameMono() {
        return Mono.just("yashas").log();
    }

    public Mono<List<String>> nameMono_flatmap() {
        return Mono.just("yashas")
                .flatMap(s -> Mono.just(Arrays.asList(s.split(""))))
                .log();
    }

    public Flux<String> nameMono_flatmapMany() {
        return Mono.just("yashas")
                .flatMapMany(s -> splitStringToFlux(s))
                .log();
    }

    public Flux<String> namesFlux_transform(int strLength) {
        return Flux.fromIterable(namesList)
                .transform()
                .filter(s -> s.length() > strLength)
                .flatMap(s -> splitStringToFlux(s)).log();
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
