package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Function;

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
        Function<Flux<String>, Flux<String>> function = name -> name.map(n -> n.toUpperCase(Locale.ROOT)).filter(s -> s.length() > strLength);

        return Flux.fromIterable(namesList)
                .transform(function)
                .defaultIfEmpty("default").log();
    }

    public Flux<String> namesFlux_transform_switchifempty(int strLength) {
        Function<Flux<String>, Flux<String>> function = name -> name.map(n -> n.toUpperCase(Locale.ROOT)).filter(s -> s.length() > strLength);
        return Flux.fromIterable(namesList)
                .transform(function)
                .switchIfEmpty(Flux.fromIterable(Arrays.asList("x", "x", "x")))
                .defaultIfEmpty("default").log();
    }

    public Flux<String> explore_concat() {
        var flux1 = Flux.just("A", "B", "C");
        var flux2 = Flux.just("D", "E", "F");

        return Flux.concat(flux1, flux2).log();
    }

    public Flux<String> explore_concatwith() {
        var mono = Mono.just("A");
        var flux2 = Flux.just("D", "E", "F");

        return mono.concatWith(flux2).log();
    }

    public Flux<String> explore_merge() {
        var flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
        var flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(120));

        return Flux.merge(flux1, flux2).log();
    }

    public Flux<String> explore_mergewith() {
        var mono = Mono.just("A");
        var flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(150));

        return mono.mergeWith(flux2).log();
    }

    public Flux<String> explore_merge_sequestial() {
        var flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
        var flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(120));

        return Flux.mergeSequential(flux1, flux2).log();
    }

    public Flux<String> explore_zip() {
        var flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
        var flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(160));

        return Flux.zip(flux1, flux2, (a, b) -> a + b).log();
    }

    public Flux<String> explore_zip_1() {
        var flux1 = Flux.just("A", "B", "C");
        var flux2 = Flux.just("D", "E", "F");
        var flux3 = Flux.just("1", "2", "3");
        var flux4 = Flux.just("4", "5", "6");

        return Flux.zip(flux1, flux2, flux3, flux4).map(tuple4 -> tuple4.getT1() + tuple4.getT2() + tuple4.getT3() + tuple4.getT4()).log();
    }

    public Flux<String> explore_zipwith() {
        var flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
        var flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(160));

        return flux1.zipWith(flux2, (a, b) -> a + b).log();
    }

    public Mono<String> explore_zipwith_mono() {
        var mono1 = Mono.just("A").delayElement(Duration.ofMillis(100));
        var mon02 = Mono.just("D").delayElement(Duration.ofMillis(160));

        return mono1.zipWith(mon02, (a, b) -> a + b).log();
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
