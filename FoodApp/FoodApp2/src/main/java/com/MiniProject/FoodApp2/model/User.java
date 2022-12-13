package com.MiniProject.FoodApp2.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private int userId;
    private String UserName;
    private String mobileNumber;
    private String emailId;
    private String password;
    private String address;
    private double latitude;
    private double longitude;

}
