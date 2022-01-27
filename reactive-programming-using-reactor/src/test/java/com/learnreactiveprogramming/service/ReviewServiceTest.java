package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class ReviewServiceTest {

    WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080/movies").build();
    ReviewService reviewService = new ReviewService(webClient);

    @Test
    void retrieveReviewsFlux_RestClient() {
        var flux= reviewService.retrieveReviewsFlux_RestClient(1);
        StepVerifier.create(flux)
//                .expectNextCount(1)
                .assertNext(review -> {
                    Assertions.assertEquals("Nolan is the real superhero",review.getComment());
                    Assertions.assertEquals(8.2, review.getRating());
                })
                .verifyComplete();
    }
}
