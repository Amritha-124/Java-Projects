package com.MiniProject.FoodApp2.controller;

import com.MiniProject.FoodApp2.model.Admin;
import com.MiniProject.FoodApp2.model.Dish;
import com.MiniProject.FoodApp2.model.Menu;
import com.MiniProject.FoodApp2.model.Restaurants;
import com.MiniProject.FoodApp2.service.RestaurantService;
import com.MiniProject.FoodApp2.service.RestaurantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/admin")
public class RestaurantController {
    @Autowired
    RestaurantServiceImpl restaurantService;


    @PostMapping("/adminSignUp")
    public ResponseEntity<String> adminSignUp(@RequestBody Admin admin)
    {
        if(restaurantService.adminSignUp(admin).equalsIgnoreCase("failed or already registered"))
        {
            return  new ResponseEntity<>("failed or already registered",HttpStatus.ALREADY_REPORTED);
        }
       return  new ResponseEntity<>("admin registered successfully", HttpStatus.CREATED);
    }

    @GetMapping("/adminSignIn/{emailId}/{password}")
    public ResponseEntity<Integer> adminSignIn(@PathVariable String emailId, @PathVariable String password) {
        return new ResponseEntity<>(restaurantService.adminSignIn(emailId, password), HttpStatus.OK);
    }
    @PostMapping("/addRestaurant/{sID}")
    public ResponseEntity<String> addRestaurants(@ModelAttribute  Restaurants restaurants, @PathVariable int sID)
    {
        if(restaurantService.addRestaurants(restaurants,sID).equalsIgnoreCase("added successfully"))
        {

            return new ResponseEntity<>("added successfully",HttpStatus.CREATED);
        }
        return new ResponseEntity<>("already registered or invalid credentials",HttpStatus.ALREADY_REPORTED);

    }
    @PostMapping("/addDish/{sID}")
    public ResponseEntity<String> addDish(@ModelAttribute Dish dish, @PathVariable int sID) throws Exception {
//        if(restaurantService.addDish(dish,sID).equalsIgnoreCase("added successfully"))
//        {
            return new ResponseEntity<>(restaurantService.addDish(dish,sID),HttpStatus.CREATED);

//        }else {
//            return new ResponseEntity<>("already registered or invalid credentials", HttpStatus.ALREADY_REPORTED);
//        }

    }
    @PostMapping("/addMenu/{sID}")
    public ResponseEntity<String> addMenu(@RequestBody Menu menu, @PathVariable int sID) throws Exception {
        if(restaurantService.addMenu(menu,sID).equalsIgnoreCase("already added or invalid credentials"))
        {
            return new ResponseEntity<>("already registered or invalid credentials",HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<>("added successfully",HttpStatus.CREATED);
    }


}
