package com.MiniProject.FoodApp2.service;

import com.MiniProject.FoodApp2.model.Admin;
import com.MiniProject.FoodApp2.model.Dish;
import com.MiniProject.FoodApp2.model.Menu;
import com.MiniProject.FoodApp2.model.Restaurants;

public interface RestaurantService {
    String adminSignUp(Admin admin);
    int adminSignIn(String emailId, String password);
    String addRestaurants(Restaurants restaurants, int sID);
    String addDish(Dish dish,int sID) throws Exception;
    String addMenu(Menu menu,int sID);

}
