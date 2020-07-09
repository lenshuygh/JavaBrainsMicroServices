package be.lens.microservices.javabrains.moviecatalogservice.resources;

import be.lens.microservices.javabrains.moviecatalogservice.model.CatalogItem;
import be.lens.microservices.javabrains.moviecatalogservice.model.Movie;
import be.lens.microservices.javabrains.moviecatalogservice.model.UserRating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/{userId}")
    @HystrixCommand(fallbackMethod = "getFallbackCatalog")
    public List<CatalogItem> getCatalog(@PathVariable String userId) {
        //get all rated movie IDs
        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class);
        return ratings.getRatings().stream()
                .map(rating -> {
                    // for each movie ID call the info service and get the details
                    Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
                    /*Movie movie = webClientBuilder.build()
                            .get()
                            .uri("http://localhost:8082/movies/" + rating.getMovieId())
                            .retrieve()
                            .bodyToMono(Movie.class)
                            .block();*/
                    // put them all together
                    return new CatalogItem(movie.getName(), "A whole lotta dreaming inside dreaming and action too", rating.getRating());
                })
                .collect(Collectors.toList());
    }

    public List<CatalogItem> getFallbackCatalog(@PathVariable String userId) {
        return Arrays.asList(new CatalogItem("No movie","",0));
    }
}
