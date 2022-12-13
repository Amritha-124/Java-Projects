package com.MiniProject.FoodApp2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private int dishId;
    private int quantity;
    private int cartId;

}
