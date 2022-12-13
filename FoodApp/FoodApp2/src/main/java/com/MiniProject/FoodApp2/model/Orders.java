package com.MiniProject.FoodApp2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    private int orderId;
    private int cartId;
    private long cardNo;
    private int Cvv;
    private Date expiryDate;
    private String deliveryAddress;
    private String orderStatus;
}
