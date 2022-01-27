package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Review;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

public class ReviewService {

    private WebClient webClient;

    public ReviewService(WebClient webClient) {
        this.webClient = webClient;
    }

    public ReviewService() {
    }

    public Flux<Review> retrieveReviewsFlux_RestClient (long movieInfoId) {
        return webClient.get().uri(uriBuilder -> {
                    return uriBuilder.path("/v1/reviews")
                            .queryParam("movieInfoId", movieInfoId).build();
                })
                .retrieve()
                .bodyToFlux(Review.class);
    }

    public List<Review> retrieveReviews(long movieInfoId) {

        return List.of(new Review(1L, movieInfoId, "Awesome Movie", 8.9),
                new Review(2L, movieInfoId, "Excellent Movie", 9.0));
    }

    public Flux<Review> retrieveReviewsFlux(long movieInfoId) {

        var reviewsList = List.of(new Review(1L, movieInfoId, "Awesome Movie", 8.9),
                new Review(2L, movieInfoId, "Excellent Movie", 9.0));
        return Flux.fromIterable(reviewsList);
    }

}
