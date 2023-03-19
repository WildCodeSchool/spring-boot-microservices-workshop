package io.javabrains.ratingsdataservice.resources;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.ratingsdataservice.model.Rating;
import io.javabrains.ratingsdataservice.model.UserRating;

@RestController
@RequestMapping("/ratingsdata")
public class RatingsResource {

    @RequestMapping("/movies/{movieId}")
    public Rating getMovieRating(@PathVariable("movieId") String movieId) {
        return new Rating(movieId, 4);
    }

    @RequestMapping("/user/{userId}")
    public UserRating getUserRatings(@PathVariable("userId") String userId) {
        UserRating userRating = new UserRating();
        userRating.initData(userId);
        return userRating;

    }

    record AddUserRatingDTO(String movieId, int rating) {

    }

    Logger log = LoggerFactory.getLogger(getClass());

    @PostMapping("/user/{userId}")
    public UserRating addUserRating(@PathVariable("userId") String userId, @RequestBody AddUserRatingDTO params) {
        log.info("CREATE RATING FOR user" + userId + " params: " + params);
        UserRating userRating = new UserRating();
        userRating.setUserId(userId);
        userRating.setRatings(List.of(new Rating(params.movieId(), params.rating())));
        return userRating;
    }

}
