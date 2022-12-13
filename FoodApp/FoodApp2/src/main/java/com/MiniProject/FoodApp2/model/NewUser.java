package com.MiniProject.FoodApp2.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor

public class NewUser {
    private String mobileNumber;

    @JsonCreator
    public NewUser(String mobileNumber)
    {
        this.mobileNumber = mobileNumber;
    }
}
