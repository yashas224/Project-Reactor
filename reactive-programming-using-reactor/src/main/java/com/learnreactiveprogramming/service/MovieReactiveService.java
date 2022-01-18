package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.Review;
import com.learnreactiveprogramming.exception.MovieException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class MovieReactiveService {

    private MovieInfoService movieInfoService;
    private ReviewService reviewService;

    public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService) {
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
    }

    public Flux<Movie> getAllMovies() {
        // error behaviour: throw MvieException when either of the calls fail
        var movieInfoFLux = movieInfoService.retrieveMoviesFlux();
        return movieInfoFLux.flatMap(movieInfo -> {
            Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId()).collectList();
            return reviewsMono.map(reviewList -> new Movie(movieInfo, reviewList));
        }).onErrorMap(ex -> {
            log.error("Exceptiuon is -" + ex);
            return new MovieException(ex);
        });
    }

    public Flux<Movie> getAllMovies_Retry() {
        // error behaviour: throw MvieException when either of the calls fail
        var movieInfoFLux = movieInfoService.retrieveMoviesFlux();
        return movieInfoFLux.flatMap(movieInfo -> {
            Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId()).collectList();
            return reviewsMono.map(reviewList -> new Movie(movieInfo, reviewList));
        }).onErrorMap(ex -> {
            log.error("Exceptiuon is -" + ex);
            return new MovieException(ex);
        }).retry(3);
    }




    public Mono<Movie> getMovieById(long movieId) {
        var monoMovieInfo = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        var reviews = reviewService.retrieveReviewsFlux(movieId).collectList();
        return Mono.zip(monoMovieInfo, reviews, (info, rev) -> new Movie(info, rev));
    }

    public Mono<Movie> getMovieByIdFlatMap(long movieId) {
        var monoMovieInfo = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        return monoMovieInfo.flatMap(movieInfo -> {
            var monoReview = reviewService.retrieveReviewsFlux(movieId).collectList();
            return monoReview.map(reviews -> new Movie(movieInfo, reviews));
        });

    }
}
