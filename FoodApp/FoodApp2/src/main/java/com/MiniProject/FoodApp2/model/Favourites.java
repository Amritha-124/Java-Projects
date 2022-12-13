package com.MiniProject.FoodApp2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Favourites {
    private int userId;
    private int restaurantId;
}
