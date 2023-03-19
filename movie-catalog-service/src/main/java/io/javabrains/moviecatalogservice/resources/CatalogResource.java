package io.javabrains.moviecatalogservice.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {

    private static final String MOVIE_INFO_SERVICE_HTTP_URLBASE = "http://movie-info-service/movies";
    private static final String RATINGS_SERVICE_HTTP_URLBASE = "http://ratings-data-service/ratingsdata";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    WebClient.Builder webClientBuilder;
    
    record AddItemDTO(int rating, String title, String description) {
    }

    @PostMapping("/{userId}")
    public CatalogItem newItem(@PathVariable("userId") String userId, @RequestBody AddItemDTO params) {

        Movie movie = restTemplate.postForObject(MOVIE_INFO_SERVICE_HTTP_URLBASE, params, Movie.class);

        record RegisterRatingDTO(String movieId, int rating) {
        }
        UserRating userRating = restTemplate.postForObject(
            RATINGS_SERVICE_HTTP_URLBASE + "/user/" + userId, 
            new RegisterRatingDTO(movie.getMovieId(), params.rating()), 
            UserRating.class);

        return new CatalogItem(movie.getName(), movie.getDescription(), userRating.getRatings().get(0).getRating());
    }

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating userRating = restTemplate.getForObject(RATINGS_SERVICE_HTTP_URLBASE + "/user/" + userId, UserRating.class);

        return userRating.getRatings().stream()
                .map(rating -> {
                    Movie movie = restTemplate.getForObject(MOVIE_INFO_SERVICE_HTTP_URLBASE + "/" + rating.getMovieId(), Movie.class);
                    return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
                })
                .collect(Collectors.toList());

    }
}
