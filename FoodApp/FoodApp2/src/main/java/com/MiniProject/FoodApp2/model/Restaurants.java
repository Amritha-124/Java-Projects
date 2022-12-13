package com.MiniProject.FoodApp2.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Restaurants {
    private int restaurantId;
    private String restaurantName;
    private String address;
    private Time  openingTime;
    private Time closingTime;
    private double latitude;
    private double longitude;
    private String deliveryType;
    private double overallRating;
    private MultipartFile restaurantImage;
    private String imageURL;
    private int ratings;
    private int favourites;

    public Restaurants(String restaurantName, String address, String deliveryType, double overallRating, String imageURL, int ratings, int favourites) {
        this.restaurantName = restaurantName;
        this.address = address;
        this.deliveryType = deliveryType;
        this.overallRating = overallRating;
        this.imageURL = imageURL;
        this.ratings = ratings;
        this.favourites = favourites;
    }
}
