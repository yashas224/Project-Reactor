package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class MovieReactiveServiceTest {
    private MovieInfoService movieInfoService = new MovieInfoService();
    private ReviewService reviewService = new ReviewService();
    private RevenueService revenueService = new RevenueService();
    private MovieReactiveService movieReactiveService = new MovieReactiveService(movieInfoService, reviewService, revenueService);


    @Test
    void getAllMovies() {
        var movieFlux = movieReactiveService.getAllMovies().log();

        StepVerifier.create(movieFlux)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .assertNext(movie -> {
                    assertEquals("The Dark Knight", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .assertNext(movie -> {
                    assertEquals("Dark Knight Rises", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .verifyComplete();
    }

    @Test
    void getMovieById() {
        var movieMono = movieReactiveService.getMovieById(45l).log();

        StepVerifier.create(movieMono)
                .assertNext(movie -> {
                    assertEquals(45, movie.getMovie().getMovieInfoId());
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .verifyComplete();
    }

    @Test
    void getMovieByIdWithRevenue() {
        var movieMono = movieReactiveService.getMovieByIdWithRevenue(45l).log();

        StepVerifier.create(movieMono)
                .assertNext(movie -> {
                    assertEquals(45, movie.getMovie().getMovieInfoId());
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                    assertNotNull(movie.getRevenue());
                })
                .verifyComplete();
    }
}