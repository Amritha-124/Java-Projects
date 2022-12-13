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
public class MenuPanel            //for category
{
    private String dishCategory;
    private Integer noOfItems;
    private List<DishResponse> DishResponses;
}
