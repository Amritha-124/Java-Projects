package com.MiniProject.FoodApp2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    private int cardNo;
    private int userId;
    private int CVV;
    private Date expiryDate;
}
