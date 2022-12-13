package com.MiniProject.FoodApp2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cuisines {
    private String cuisines;
    private List<RestaurantList> restaurantLists;
}
