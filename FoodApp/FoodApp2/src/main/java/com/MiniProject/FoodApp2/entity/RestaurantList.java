package com.MiniProject.FoodApp2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantList
{
    private int restaurantId;
    private String restaurantName;
    private String address;
    private String deliveryType;
    private MultipartFile restaurantImage;
    private String imageURL;
    private int ratings;
    private int favourites;
}
