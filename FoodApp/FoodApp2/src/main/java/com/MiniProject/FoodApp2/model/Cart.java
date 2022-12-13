package com.MiniProject.FoodApp2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private int cartId;
    private int userId;
    private int restaurantId;
    private double totalAmount;
}
