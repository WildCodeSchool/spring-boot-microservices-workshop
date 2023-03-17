package io.javabrains.movieinfoservice.resources;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javabrains.movieinfoservice.models.Movie;

@RestController
@RequestMapping("/movies")
public class MovieResource {

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable("movieId") String movieId) {

        String randomTitle = fetchMovieTitle();
        return new Movie(movieId, randomTitle, "A sample description of movie: " + randomTitle);

    }

    private String fetchMovieTitle() {
        try {
            var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
            var request = HttpRequest
                    .newBuilder(
                            new URI("https://story-shack-cdn-v2.glitch.me/generators/random-movie-generator?count=2"))
                    .GET()
                    .build();
            String jsonResponse = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            var response = new ObjectMapper().readValue(jsonResponse, MovieGeneratorResponse.class);
            return response.data.get(0).name;


        } catch (Exception e) {
            throw new RuntimeException("cannot fetch movie name", e);
        }
        /*
        Alternative org.springframework.web.reactive.function.client.WebClient way
        Movie movie = webClientBuilder.build().get().uri("http://localhost:8082/movies/"+ rating.getMovieId())
        .retrieve().bodyToMono(Movie.class).block();
        */
    }

    private static class MovieGeneratorResponse {
        static class Item {
            public String name;
        }

        public List<Item> data;
    }
}
