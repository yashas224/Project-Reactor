package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.MovieInfo;
import com.learnreactiveprogramming.exception.MovieException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Incubating;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MovieReactiveServiceMockTest {

    @Mock
    private MovieInfoService movieInfoService;
    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private MovieReactiveService movieReactiveService;


    @Test
    void getAllMovies() {
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();
        Mockito.when(reviewService.retrieveReviewsFlux(Mockito.anyLong())).thenCallRealMethod();
        var movieFlux = movieReactiveService.getAllMovies();
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
    void getAllMovies_Exception() {
        String errorMessage = "Exception Occured in review Service";
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();
        Mockito.when(reviewService.retrieveReviewsFlux(Mockito.anyLong())).thenThrow(new RuntimeException(errorMessage));
        var movieFlux = movieReactiveService.getAllMovies().log();
        StepVerifier.create(movieFlux)
                .expectError(MovieException.class)
                .verify();
    }

    @Test
    void getAllMovies_Retry() {
        String errorMessage = "Exception Occured in review Service";
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();
        Mockito.when(reviewService.retrieveReviewsFlux(Mockito.anyLong())).thenThrow(new RuntimeException(errorMessage));
        var movieFlux = movieReactiveService.getAllMovies_Retry().log();
        StepVerifier.create(movieFlux)
                .expectError(MovieException.class)
                .verify();

        Mockito.verify(reviewService,Mockito.times(4)).retrieveReviewsFlux(Mockito.anyLong());
    }

}
