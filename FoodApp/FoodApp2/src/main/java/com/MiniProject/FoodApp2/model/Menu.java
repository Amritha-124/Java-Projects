package com.MiniProject.FoodApp2.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Menu {
    private int dishId;
    private int restaurantId;
    private double price;
}
