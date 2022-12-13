package com.MiniProject.FoodApp2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class Home {
    private String restaurantName;
    private String address;
    private String deliveryType;
    private double overallRating;
    private String imageURL;
    private int ratings;
    private int favourites;

    public Home(String restaurantName, String address, String deliveryType, double overallRating, String imageURL, int ratings, int favourites) {
        this.restaurantName = restaurantName;
        this.address = address;
        this.deliveryType = deliveryType;
        this.overallRating = overallRating;
        this.imageURL = imageURL;
        this.ratings = ratings;
        this.favourites = favourites;
    }
}
