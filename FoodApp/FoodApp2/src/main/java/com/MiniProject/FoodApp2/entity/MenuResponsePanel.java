package com.MiniProject.FoodApp2.entity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuResponsePanel
{
    private String restaurantName;
    private String address;
    private Float ratings;
    private Float overallRating;
    private String imageURL;
    private String deliveryType;
    List<MenuPanel> menuPanelList;
}