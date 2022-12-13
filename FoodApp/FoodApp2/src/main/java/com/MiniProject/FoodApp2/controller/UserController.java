package com.MiniProject.FoodApp2.controller;


import com.MiniProject.FoodApp2.Utility.JWTUtility;
import com.MiniProject.FoodApp2.entity.*;
import com.MiniProject.FoodApp2.model.*;
import com.MiniProject.FoodApp2.service.SmsService;
import com.MiniProject.FoodApp2.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController

public class UserController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    SmsService smsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtility jwtUtility;


    @PostMapping("/user/addMobileNumber")
    public ResponseEntity<String> addMobileNumber(@RequestBody NewUser newUser){
        return new ResponseEntity<>(userService.addMobileNumber(newUser),HttpStatus.CREATED) ;
    }

    @PostMapping("/user/userSignUp/{mobileNumber}")
    public ResponseEntity<String> UserSignUp(@RequestBody User user, @PathVariable String mobileNumber)
    {
        if(userService.UserSignUp(user,mobileNumber).equalsIgnoreCase("already registered"))
        {
            return new ResponseEntity<>("already registered",HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<>("successfully registered", HttpStatus.CREATED);
    }


    @PutMapping("/user/send2faCodeInSMS/{mobileNumber}")
    public ResponseEntity<Object> send2faCodeInSMS(@RequestBody String phoneNumber, @PathVariable String mobileNumber) {
        String tfaCode = String.valueOf(new Random().nextInt(9999) + 1000);
        smsService.sendSms(phoneNumber, tfaCode);
        userService.update2FAProperties(mobileNumber, tfaCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/user/verify")
    public ResponseEntity<Object> verify(@RequestParam String mobileNumber, @RequestParam String code) {
        boolean isValid = userService.checkCode(mobileNumber, code);
        if (isValid) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @PostMapping("/authenticate")
    public JWTResponse authenticate(@RequestBody JWTRequest jwtRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getEmailId(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getEmailId());

        final String token = jwtUtility.generateToken(userDetails);

        return new JWTResponse(token);
    }

    @PutMapping("/user/forgetPasswordCode/{mobileNumber}")
    public ResponseEntity<Object> forgetPassword(@RequestBody String phoneNumber, @PathVariable String mobileNumber) {
        String tfaCode = String.valueOf(new Random().nextInt(9999) + 1000);
        smsService.sendSms(phoneNumber, tfaCode);
        userService.forgetPassword(mobileNumber, tfaCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/user/resetPassword")
    public ResponseEntity<Object> verify(@RequestParam String mobileNumber, @RequestParam String code,@RequestParam String password) {
        boolean isValid = userService.resetPassword(mobileNumber, code,password);
        if (isValid) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }
    @GetMapping("/getRestaurant/{restaurantName}")
    public ResponseEntity<List<Restaurants>> getRestaurantByName(@PathVariable String restaurantName)
    {
        if(userService.getRestaurantByName(restaurantName).size()>0)
        {
            return new ResponseEntity<>(userService.getRestaurantByName(restaurantName),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

    }

    @GetMapping("/getAllDishes")
    public ResponseEntity<List<Dish>> viewAllDishes()
    {
        if(userService.viewAllDishes().size()>0)
        {
            return new ResponseEntity<>(userService.viewAllDishes(),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

    }
   @PostMapping("/giveRating")
    public ResponseEntity<String> addRating(@RequestBody Rating rating) {
        if (userService.addRating(rating).equalsIgnoreCase("rated successfully")) {

        return new ResponseEntity<>("rated successfully",HttpStatus.CREATED);
        }

            return new ResponseEntity<>("you have already rated or invalid credentials", HttpStatus.ALREADY_REPORTED);

    }
    @PostMapping("/addFavourites")
    public ResponseEntity<String> addToFavourites(@RequestBody Favourites favourites)
    {
        if (userService.addToFavourites(favourites).equalsIgnoreCase("added to favourites")) {

            return new ResponseEntity<>("added to favourites",HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Deleted from favourites", HttpStatus.ALREADY_REPORTED);


    }
    @GetMapping("/getRestaurantByCuisines/{cuisines}")
    public ResponseEntity<Cuisines> getCuisinePanel(@PathVariable String cuisines)
    {
        return new ResponseEntity<>(userService.getCuisinePanel(cuisines),HttpStatus.CREATED);
    }
    @PostMapping("/addAddress")
    public ResponseEntity<String> addAddresses(@RequestBody Address address)
    {
        String query = userService.addAddress(address);
        if (query.equalsIgnoreCase("added successfully")) {
            return new ResponseEntity<>(query, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(query, HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/getImageURL/{restaurantName}")
    public ResponseEntity<String> getImageUrl(@PathVariable String restaurantName) {
        return new ResponseEntity<>(userService.getImageUrl(restaurantName), HttpStatus.OK);
    }


    @GetMapping("/getRestaurantPic/{restaurantName}")
    public ResponseEntity<Resource> getRestaurantImage(@PathVariable String restaurantName) throws Exception {
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/png")).header("Content-Disposition", "filename=\"" + restaurantName + ".png" + "\"").body(new ByteArrayResource(userService.getRestaurantPic(restaurantName)));
    }
    @GetMapping("/getPhotoURL/{dishName}")
    public ResponseEntity<String> getPhotoUrl(@PathVariable String dishName) {
        return new ResponseEntity<>(userService.getPhotoUrl(dishName), HttpStatus.OK);
    }


    @GetMapping("/getDishPic/{dishName}")
    public ResponseEntity<Resource> getDishPhoto(@PathVariable String dishName) throws Exception {
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/png")).header("Content-Disposition", "filename=\"" +dishName + ".png" + "\"").body(new ByteArrayResource(userService.getDishPic(dishName)));
    }


    @GetMapping("/getRestaurantsByRating")
    public ResponseEntity<List<Restaurants>> RestaurantsByRating()
    {
        if(userService.getRestaurantsByRating()!= null)
        {
            return new ResponseEntity<>(userService.getRestaurantsByRating(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getByFreeDelivery/{deliveryType}")
    public List<Restaurants> getRestaurantByDeliveryType(@PathVariable String deliveryType)
    {
        return userService.getRestaurantByDeliveryType(deliveryType);
    }
    @GetMapping("/Home")
    public ResponseEntity<List<Home>> Home()
    {
        if(userService.Home()!= null)
        {
            return new ResponseEntity<>(userService.Home(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }
    @GetMapping("/TopDishes")
    public ResponseEntity<List<TopDishResponse>> viewTopDishes()
    {
        return new ResponseEntity<>(userService.viewTopDishes(),HttpStatus.OK);
    }
    @GetMapping("/getRestaurantMenu")
    public ResponseEntity<?> getRestaurantDetails(@RequestParam Integer restaurantId)
    {
        return new ResponseEntity<>(userService.getRestaurantDetails(restaurantId),HttpStatus.OK);
    }

//    @PostMapping("/addCart")
//    public String addToCart(@RequestBody  CartInfo cartInfo)
//    {
//        return userService.addToCart(cartInfo);
//    }

    @PostMapping("/addCart")
    public ResponseEntity<?> addToOrder(@RequestBody CartResponse cartResponse) {
        return new ResponseEntity<>(userService.addToOrder(cartResponse), HttpStatus.CREATED);
    }

    @GetMapping("/viewCart")
    public ResponseEntity<List<Cart>> viewCart()
    {
        if (userService.viewCart().size() > 0) {
            return new ResponseEntity<>(userService.viewCart(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addCard")
    public ResponseEntity< String> addCardDetails(@RequestBody Card card)
    {
        String query=userService.addCardDetails(card);
        if(query.equalsIgnoreCase("added successfully"))
        {
            return new ResponseEntity<>(query,HttpStatus.OK);

        }
        return new ResponseEntity<>(query,HttpStatus.ALREADY_REPORTED);
    }
    @GetMapping("/ViewFavourites")
    public ResponseEntity<List>ViewFavourites()
    {
       List query=userService.viewFavourites();
       if(query!= null)
       {
           return new ResponseEntity<>(query,HttpStatus.OK);
       }
       return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

    }
    @GetMapping("/getItems")
    public ResponseEntity<List<Item>> viewItems()
    {
        if (userService.viewItems().size() > 0) {
            return new ResponseEntity<>(userService.viewItems(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    @PostMapping("/ordering")
    public ResponseEntity<?> placeOrder(@RequestBody Orders orders)
    {
        return new ResponseEntity<>(userService.placeOrder(orders), HttpStatus.OK);
    }




}
