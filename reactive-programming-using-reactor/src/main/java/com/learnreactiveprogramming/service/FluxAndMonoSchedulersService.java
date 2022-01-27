package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

@Slf4j
public class FluxAndMonoSchedulersService {

    static List<String> namesList = List.of("alex", "ben", "chloe");
    static List<String> namesList1 = List.of("adam", "jill", "jack");

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
                .map(str -> str.toUpperCase(Locale.ROOT))
                .filter(s -> s.length() > strLength)
                .flatMap(s -> splitStringToFlux(s)).log();
    }

    public Flux<String> splitStringToFlux(String s) {
        return Flux.fromArray(s.split(""));
    }

    public Flux<String> namesFlux_flatMapAsync(int strLength) {
        return Flux.fromIterable(namesList)
                .map(str -> str.toUpperCase(Locale.ROOT))
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

    public Flux<String> namesFlux_do_on_callback() {
        return Flux.fromIterable(namesList)
                .map(String::toUpperCase)
                .doOnNext(data -> System.out.println("emitted data is" + data))
                .doOnSubscribe(subscription -> System.out.println("subscription is " + subscription))
                .doOnComplete(() -> System.out.println("complete signal emitted "))
                .doFinally(signalType -> System.out.println("inside finally " + signalType))
                .log();
    }

    public Flux<String> exceptionFlux() {
        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("EXCEPTION OCCURED")))
                .concatWith(Flux.just("D")).log();
    }


    // "D" is not emitted as the subscription between publisher and subscrber ends as soon as an exception is thrown
    public Flux<String> explore_onErrorReturn() {
        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new IllegalStateException("EXCEPTION OCCURED")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("E")
                .log();
    }

    public Flux<String> explore_onErrorResume(Exception e) {
        var recoveryFlux = Flux.just("X", "Y", "Z");
        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(e))
                .concatWith(Flux.just("D"))
                .onErrorResume(exception -> {
                    log.error("Exception is", exception);
                    if (exception instanceof IllegalStateException) {
                        return recoveryFlux;
                    } else {
                        return Flux.error(exception);
                    }
                })
                .log();
    }

    public Flux<String> explore_onErrorContinue() {
        var recoveryFlux = Flux.just("X", "Y", "Z");
        return Flux.just("A", "B", "C")
                .map(s -> {
                    if (s.equals("B")) {
                        throw new RuntimeException("exception during processing");
                    }
                    return s;
                })
                .concatWith(Flux.just("D"))
                .onErrorContinue((ex, val) -> {
                    log.info("error is" + ex);
                    log.info("value that caused error is " + val);
                })
                .log();
    }

    public Flux<String> explore_onErrorMap(Exception e) {
        var recoveryFlux = Flux.just("X", "Y", "Z");
//        return Flux.just("A", "B", "C")
//                .map(s -> {
//                    if (s.equals("B")) {
//                        throw new RuntimeException("exception during processing");
//                    }
//                    return s;
//                })
//                .concatWith(Flux.just("D"))
        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(e))
                .onErrorMap(ex -> {
                    log.error("Exception is -" + ex);
                    return new ReactorException(ex, ex.getMessage());
                })
                .log();
    }

    public Flux<String> explore_onErrorMap_onOperatorDebug(Exception e) {

        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(e))
//                .checkpoint("Here is the Exception thrown")
                .onErrorMap(ex -> {
                    log.error("Exception is -" + ex);
                    return new ReactorException(ex, ex.getMessage());
                })
                .log();
    }


    public Flux<String> explore_doOnError() {
        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("error occured")))
                .concatWith(Flux.just("D"))
                .doOnError(ex -> {
                    log.error("Exception is- " + ex);
                })
                .log();
    }

    public Mono<Object> explore_Mono_OnErrorReturn() {
        return Mono.just("A")
                .map(val -> {
                    throw new RuntimeException("exception occured");
                })
                .onErrorReturn("D")
                .log();
    }

    public static void main(String[] args) {
        FluxAndMonoSchedulersService fluxAndMonoSchedulersService = new FluxAndMonoSchedulersService();
        fluxAndMonoSchedulersService.namesFlux()
                .subscribe(n -> System.out.println(n));

        System.out.println("Mono Displaying");

        fluxAndMonoSchedulersService.nameMono()
                .subscribe(System.out::println);
    }


    //  expolre publishOn
    public Flux<String> explore_publishOn() {
        var namesFlux = Flux.fromIterable(namesList)
                .publishOn(Schedulers.boundedElastic())
                .map(s -> {
                    log.info("2-name is " + s);
                    return s;
                })
                .map(this::upperCase).log();
        var namesFlux1 = Flux.fromIterable(namesList1)
                .publishOn(Schedulers.parallel())
                .map(this::upperCase).log();

        return namesFlux.mergeWith(namesFlux1);
    }


    public Flux<String> explore_subscribeOn() {
        var namesFlux = flux1(namesList)
                .subscribeOn(Schedulers.boundedElastic())
                .log();
        var namesFlux1 = flux1(namesList1)
                .subscribeOn(Schedulers.boundedElastic())
                .map(s -> {
                    log.info("name is " + s);
                    return s;
                }).log();
        return namesFlux.mergeWith(namesFlux1);
    }

    private Flux<String> flux1(List<String> list) {
        return Flux.fromIterable(list)
                .map(this::upperCase);
    }

    public ParallelFlux<String> explore_parallel() {
        var cores = Runtime.getRuntime().availableProcessors();
        log.info("Avilabe Cores {}", cores);
        return Flux.fromIterable(namesList)
                .parallel()
                .runOn(Schedulers.parallel())
                .map(this::upperCase).log();
    }

    public Flux<String> explore_parallel_usingFlatmap() {
        return Flux.fromIterable(namesList)
                .flatMap(n -> Mono.just(n)
                        .map(this::upperCase)
                        .subscribeOn(Schedulers.parallel()))
                .log();
    }

    public Flux<String> explore_parallel_usingFlatmap_1() {
        var namesFlux = Flux.fromIterable(namesList)
                .flatMap(n -> Mono.just(n)
                        .map(this::upperCase)
                        .subscribeOn(Schedulers.parallel()))
                .log();
        var namesFlux1 = Flux.fromIterable(namesList1).
                flatMap(n -> Mono.just(n)
                        .map(this::upperCase)
                        .subscribeOn(Schedulers.parallel())).log();

        return namesFlux.mergeWith(namesFlux1);
    }

    public Flux<String> explore_merge_without_thread_switching() {
        var namesFlux = Flux.fromIterable(namesList)
                .map(this::upperCase).log();
        var namesFlux1 = Flux.fromIterable(namesList1)
                .map(this::upperCase).log();

        return namesFlux.mergeWith(namesFlux1);
    }


    public Flux<String> explore_parallel_usingFlatmapSequential() {
        return Flux.fromIterable(namesList)
                .flatMapSequential(n -> Mono.just(n)
                        .map(this::upperCase)
                        .subscribeOn(Schedulers.parallel()))
                .log();
    }

    // ways to  programatically creating Flux

    public Flux<Integer> explore_generate() {
        return Flux.generate(
                () -> 1,
                (state, sink) -> {
                    sink.next(state * 2);
                    if (state == 10) {
                        sink.complete();
                    }
                    return state + 1;
                });
    }


    private static List<String> getNames() {
        delay(1000);
        return namesList;
    }

    public Flux<String> explorre_create() {
        return Flux.create((fluxSink -> {
//            getNames()
//                    .forEach(name -> {
//                        fluxSink.next(name);
//                    });
            CompletableFuture
                    .supplyAsync(() -> getNames())
                    .thenAccept(name -> {
                        name.forEach(n -> {
                            fluxSink.next(n);
                            fluxSink.next(n);
                        });
                    })
                    .thenRun(() -> sendEvents(fluxSink));

        }));
    }


    public void sendEvents(FluxSink<String> fluxSink) {

        CompletableFuture
                .supplyAsync(() -> getNames())
                .thenAccept(name -> {
                    name.forEach(n -> {
                        fluxSink.next(n);
                    });
                })
                .thenRun(() -> fluxSink.complete());

    }

    public Mono<String> explorre_create_mono() {
        return Mono.create((sink -> {
            sink.success("yashas");
        }));

    }

    public Flux<String> explore_handle() {
        return Flux.fromIterable(namesList).handle((val, sink) -> {
            if (val.length() > 3) {
                sink.next(val.toUpperCase(Locale.ROOT));
            }
        });
    }


}
