package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class MovieInfoServiceTest {

    WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080/movies").build();
    MovieInfoService movieInfoService = new MovieInfoService(webClient);

    @Test
    void retriveAllMovieInfo_restClient() {
        var flux = movieInfoService.retriveAllMovieInfo_restClient();

        StepVerifier.create(flux)
                .expectNextCount(7)
                .verifyComplete();
    }

    @Test
    void retrieveMovieInfoById_RestClient() {
        var mono = movieInfoService.retrieveMovieInfoById_RestClient(1).log();
        StepVerifier.create(mono)
//                .expectNextCount(1)
                .assertNext((movieInfo) -> {
                    Assertions.assertEquals("Batman Begins", movieInfo.getName());
                })
                .verifyComplete();
    }
}
