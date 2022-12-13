package com.MiniProject.FoodApp2.service;


import com.MiniProject.FoodApp2.entity.*;
import com.MiniProject.FoodApp2.model.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;


public interface UserService {

    String addMobileNumber(NewUser newUser);

    String UserSignUp(User user,String mobileNumber);
    void update2FAProperties(String mobileNumber, String tfacode);
    boolean checkCode(String mobileNumber, String code);
    UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException;
     void forgetPassword(String mobileNumber, String tfacode);
    boolean resetPassword(String mobileNumber, String code,String password);
    List<Restaurants>getRestaurantByName(String restaurantName);
    List<Restaurants>getRestaurantByDeliveryType(String deliveryType);
    List<Dish>viewAllDishes();
    String addRating(Rating rating);
    String addToFavourites(Favourites favourites);
    Cuisines getCuisinePanel(String Cuisine);
    String addAddress(Address address);
    String getImageUrl(String restaurantName);
    byte[] getRestaurantPic(String RestaurantName) throws Exception;
    String getPhotoUrl(String dishName);
    byte[] getDishPic(String dishName) throws Exception;
    List<Restaurants>getRestaurantsByRating();
    List<Home>Home();
    MenuResponsePanel getRestaurantDetails(Integer restaurantId);
    List<TopDishResponse> viewTopDishes();
    List<Cart> viewCart();
    String addToCart(Cart cart);
    String addCardDetails(Card card);
    List <Favourites>viewFavourites();
    List<Item> viewItems();
    String placeOrder(Orders orders);







}
