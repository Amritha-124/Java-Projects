package com.MiniProject.FoodApp2.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dish {
    private int dishId;
    private  String dishName;
    private MultipartFile dishPhoto;
    private String cuisines;
    private String photoUrl;
    private String dishCategory;

}
