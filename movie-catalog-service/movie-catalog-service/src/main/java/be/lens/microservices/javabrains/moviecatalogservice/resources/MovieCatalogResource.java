package be.lens.microservices.javabrains.moviecatalogservice.resources;

import be.lens.microservices.javabrains.moviecatalogservice.model.CatalogItem;
import be.lens.microservices.javabrains.moviecatalogservice.model.UserRating;
import be.lens.microservices.javabrains.moviecatalogservice.services.resources.MovieInfo;
import be.lens.microservices.javabrains.moviecatalogservice.services.resources.UserRatingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    MovieInfo movieInfo;

    @Autowired
    UserRatingInfo userRatingInfo;

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable String userId) {
        UserRating ratings = userRatingInfo.getUserRating(userId);
        return ratings.getRatings().stream()
                .map(rating -> movieInfo.getCatalogItem(rating))
                .collect(Collectors.toList());
    }
}
