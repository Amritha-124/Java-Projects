package com.MiniProject.FoodApp2.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor

public class CartInfo
{
    private int quantity;
    private int userId;
    private int dishId;
    private int restaurantId;
    private double totalAmount;
}