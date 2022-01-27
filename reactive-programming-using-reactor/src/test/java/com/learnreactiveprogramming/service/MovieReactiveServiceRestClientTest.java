package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;


class MovieReactiveServiceRestClientTest {
    WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080/movies").build();
    ReviewService reviewService = new ReviewService(webClient);
    MovieInfoService movieInfoService = new MovieInfoService(webClient);
    MovieReactiveService movieReactiveService = new MovieReactiveService(movieInfoService, reviewService);


    @Test
    void getAllMovies_RestClient() {
        Flux<Movie> movieFLux = movieReactiveService.getAllMovies_RestClient().log();
        StepVerifier.create(movieFLux)
                .assertNext(movie -> {
                    Assertions.assertNotNull(movie.getReviewList());
                })
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void getMovieById_RestClient() {
        Mono<Movie> movieFLux = movieReactiveService.getMovieById_RestClient(1);
        StepVerifier.create(movieFLux)
                .expectNextCount(1)
                .verifyComplete();

    }
}