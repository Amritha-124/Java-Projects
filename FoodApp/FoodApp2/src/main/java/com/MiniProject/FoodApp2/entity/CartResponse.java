package com.MiniProject.FoodApp2.entity;

import com.MiniProject.FoodApp2.model.Item;
import lombok.Data;

import java.util.List;
@Data

public class CartResponse {
    private int restaurantId;
    private double totalAmount;
    List<Item> items;


}
