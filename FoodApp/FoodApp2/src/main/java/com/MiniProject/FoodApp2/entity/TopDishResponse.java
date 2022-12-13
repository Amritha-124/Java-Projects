package com.MiniProject.FoodApp2.entity;

import lombok.Data;

@Data
public class TopDishResponse {
    private int dishId;
    private String dishName;
    private String photoUrl;
    private int count;
}
