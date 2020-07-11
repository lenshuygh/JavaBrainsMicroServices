package be.lens.microservices.javabrains.moviecatalogservice.model;

import java.util.List;

public class UserRating {
    private String userId;
    private List<Rating> ratings;

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
