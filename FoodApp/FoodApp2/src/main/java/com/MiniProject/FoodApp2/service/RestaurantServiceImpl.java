package com.MiniProject.FoodApp2.service;

import com.MiniProject.FoodApp2.model.Admin;
import com.MiniProject.FoodApp2.model.Dish;
import com.MiniProject.FoodApp2.model.Menu;
import com.MiniProject.FoodApp2.model.Restaurants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Random;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    int sessionId;

    @Override
    public String adminSignUp(Admin admin) {
        try {
            jdbcTemplate.update("insert into admin (adminName,emailId,password) values(?,?,?)", admin.getAdminName(), admin.getEmailId(), admin.getPassword());
            return "admin registered successfully";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "failed or already registered";
        }
    }

    @Override
    public int adminSignIn(String emailId, String password) {
        try {

            String query = "select * from Admin where emailId= '" + emailId + "'and password='" + password + "'";
            jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Admin.class));
            sessionId = new Random().nextInt(1000);
            return sessionId;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public String addRestaurants(Restaurants restaurants, int sID) {
        if (sID == sessionId) {
            String fileName = StringUtils.cleanPath(restaurants.getRestaurantImage().getOriginalFilename());
            String downloadURL;
            try {
                if (fileName.contains("..")) {
                    throw new Exception("file name is invalid" + fileName);
                }
                downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/getRestaurantPic/")
                        .path(restaurants.getRestaurantName())
                        .toUriString();

                String imageUrl = downloadURL;
                jdbcTemplate.update("insert into restaurants(restaurantId,restaurantName,address,openingTime,closingTime,latitude,longitude,deliveryType,restaurantImage,imageURL) values(?,?,?,?,?,?,?,?,?,?)", restaurants.getRestaurantId(), restaurants.getRestaurantName(), restaurants.getAddress(), restaurants.getOpeningTime(), restaurants.getClosingTime(), restaurants.getLatitude(), restaurants.getLongitude(), restaurants.getDeliveryType(), restaurants.getRestaurantImage().getBytes(), imageUrl);
                return "added successfully";
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return "error";
            }
        }
        return "invalid id";
    }

    @Override
    public String addDish(Dish dish, int sID) throws Exception {
        if (sID == sessionId) {
            String fileName = StringUtils.cleanPath(dish.getDishPhoto().getOriginalFilename());
            String downloadURL;
            try {
                if (fileName.contains("..")) {
                    throw new Exception("file name is invalid" + fileName);
                }
                downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/getDishPic/")
                        .path(dish.getDishName())
                        .toUriString();

                String photoUrl = downloadURL;
                jdbcTemplate.update("insert into dish(dishName,dishPhoto,cuisines,photoUrl,dishCategory) values (?,?,?,?,?)",dish.getDishName(),dish.getDishPhoto().getBytes(),dish.getCuisines(),photoUrl,dish.getDishCategory());
                 return "added Dish"+dish.getDishName();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return "error";

            }
        }

        return "invalid id";
    }

    @Override
    public String addMenu(Menu menu,int sID) {
        if (sID == sessionId)
        {
           try{
               jdbcTemplate.update("insert into menu(dishId,restaurantId,price) values(?,?,?)",menu.getDishId(),menu.getRestaurantId(),menu.getPrice());
               return"added successfully";

           }catch (Exception e) {
               System.out.println(e.getMessage());
               return "already added or invalid credentials";
           }
        }

        return "invalid id";
    }


}