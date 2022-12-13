package com.MiniProject.FoodApp2.service;
import com.MiniProject.FoodApp2.entity.*;
import com.MiniProject.FoodApp2.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    int pages=2;
    int upperLimit= pages;
    int lowerLimit = 0;

    @Override
    public String addMobileNumber(NewUser newUser) {
        try {
            jdbcTemplate.update("insert into newUser (mobileNumber) values(?)", newUser.getMobileNumber());
            return "registered phone number verify it";
        }  catch (Exception e) {
            System.out.println(e.getMessage());
            return "failed";
        }
    }

    @Override
    public String UserSignUp(User user,String mobileNumber) {
        try {
            Boolean registered = jdbcTemplate.queryForObject("select registered from newUser where mobileNumber='" + mobileNumber + "'", Boolean.class);

            if (registered == true) {
                jdbcTemplate.update("insert into user(username,mobileNumber,emailId,password,address,latitude,longitude) values(?,?,?,?,?,?,?)", user.getUserName(), user.getMobileNumber(), user.getEmailId(), user.getPassword(), user.getAddress(), user.getLatitude(), user.getLongitude());
                return "user registered successfully";
            }
            return "not valid mobileNumber";
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return "already registered";
        }
    }

    public void update2FAProperties(String mobileNumber, String tfacode)
    {
        jdbcTemplate.update("update newUser set 2faCode=?, 2faExpirytime=? where mobileNumber=?", new Object[]
                {
                        tfacode, (System.currentTimeMillis() / 1000) + 60, mobileNumber
                });
    }

    //    #######################################VERIFY TFA CODE AND UPDATING PASSWORD#################################################
    public boolean checkCode(String mobileNumber, String code)
    {
        try {
            boolean query = jdbcTemplate.queryForObject("select count(*) from newUser where 2faCode=? and mobileNumber=? and 2faExpiryTime>=?", new Object[]{code, mobileNumber, System.currentTimeMillis() / 1000}, Integer.class) > 0;
            String reset_pass = "update newUser set registered = true where mobileNumber = '"+mobileNumber+"'";
            jdbcTemplate.update(reset_pass);
            System.out.println("updated password");
            return query;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException
    {
        String mailId = jdbcTemplate.queryForObject("select emailId from user where emailId=?", String.class, new Object[]{emailId});
        String password = jdbcTemplate.queryForObject("select password from user where emailId=?", String.class, new Object[]{emailId});
        return new org.springframework.security.core.userdetails.User(emailId, password, new ArrayList<>());
    }

    public void forgetPassword(String mobileNumber, String tfacode)
    {
        jdbcTemplate.update("update user set 2FAcode=?, 2FAexpiryTime=? where mobileNumber=?", new Object[]
                {
                        tfacode, (System.currentTimeMillis() / 1000) + 60, mobileNumber
                });
    }

    public boolean resetPassword(String mobileNumber, String code,String password)
    {
        try {
            boolean query = jdbcTemplate.queryForObject("select count(*) from user where 2FAcode=? and mobileNumber=? and 2FAexpiryTime>=?", new Object[]{code, mobileNumber, System.currentTimeMillis() / 1000}, Integer.class) > 0;
            String reset_pass = "update user set password ='" + password + "' where mobileNumber ='" + mobileNumber + "'";
            jdbcTemplate.update(reset_pass);
            System.out.println("updated password");
            return query;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Restaurants> getRestaurantByName(String restaurantName) {
        try
        {
         return jdbcTemplate.query("select restaurantId,restaurantName,address,openingTime,closingTime,latitude,longitude,deliveryType,overallRating,imageURL from restaurants where restaurantName=?",new BeanPropertyRowMapper<>(Restaurants.class),restaurantName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Restaurants> getRestaurantByDeliveryType(String deliveryType) {
        try
        {
            return jdbcTemplate.query("select restaurantId,restaurantName,address,openingTime,closingTime,latitude,longitude,deliveryType,overallRating,imageURL from restaurants where deliveryType=?",new BeanPropertyRowMapper<>(Restaurants.class),deliveryType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Dish> viewAllDishes() {
      try
      {
          return jdbcTemplate.query("select dishId,dishName,cuisines,photoUrl from dish ",new BeanPropertyRowMapper<>(Dish.class));
      } catch (Exception e) {
          e.printStackTrace();
          return null;
      }
    }

    @Override
    public String addRating(Rating rating)
    {
        int userId = getUserIdFromEmail();
        if(rating.getRating() <= 5 && rating.getRating() > 0) {
        try {
            jdbcTemplate.update("insert into rating(userId,restaurantId,rating) values(?,?,?)", userId ,rating.getRestaurantId(), rating.getRating());
            String query = "update restaurants rt set rt.ratings=rt.ratings+1 , rt.overallRating=(select avg(r.rating) from rating r where r.restaurantId=?) where rt.restaurantId=?" ;
            jdbcTemplate.update(query,rating.getRestaurantId(), rating.getRestaurantId());
            return "rated successfully";
        } catch (Exception e) {
            System.out.println(e);
            return "you have already rated or invalid credentials";
        }
    }
        return "Invalid Rating";
    }

    @Override
    public String addToFavourites(Favourites favourites) {
        {
            int userId = getUserIdFromEmail();
            try {
                jdbcTemplate.update("insert into favourite(restaurantId,userId) values(?,?)", favourites.getRestaurantId(), userId);
                String query = "update restaurants set favourites=favourites+1  where restaurantId=?" ;
                jdbcTemplate.update(query,favourites.getRestaurantId());
                return "added to favourites";
            }catch (Exception e)
            {
                jdbcTemplate.update("delete from favourite where restaurantId = ? and userId= ?", favourites.getRestaurantId(), userId);
                String query1 = "update restaurants set favourites=favourites-1  where restaurantId=?" ;
                jdbcTemplate.update(query1,favourites.getRestaurantId());
                return "Deleted from favourites";
            }
        }
    }



    @Override
    public Cuisines getCuisinePanel(String cuisines) {
        try {
            Cuisines cuisine = jdbcTemplate.queryForObject("select distinct cuisines as cuisines from Dish where cuisines = ?", new BeanPropertyRowMapper<>(Cuisines.class), cuisines);
            List<Integer> dishIds = jdbcTemplate.queryForList("SELECT dishId FROM dish WHERE cuisines = ?", Integer.class,cuisines);
            List<RestaurantList> restaurantLists1 = new ArrayList<>();
            for(Integer dishId:dishIds)
            {
                System.out.println(dishId);

                List<Integer> restaurantIds = jdbcTemplate.queryForList("select restaurantId from menu where dishId = ? ", Integer.class,dishId);


                for(Integer restaurantId:restaurantIds)
                {
                    List<RestaurantList> restaurantLists = jdbcTemplate.query("select restaurantId,restaurantName,address,deliveryType,overAllRating,ImageUrl,ratings,favourites from restaurants where restaurantId = ?", new BeanPropertyRowMapper<>(RestaurantList.class),restaurantId);

                    restaurantLists1.addAll(restaurantLists);
                }
            }
            cuisine.setRestaurantLists(restaurantLists1);
            return cuisine;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public String addAddress(Address address) {
        int userId = getUserIdFromEmail();
        try
        {
            jdbcTemplate.update ("insert into address(userId,address) values(?,?)",userId,address.getAddress());
            return "added successfully";

        }catch (Exception e)
        {
            System.out.println(e);
            return "invalid credentials";
        }

    }

    @Override
    public String getImageUrl(String restaurantName) {
        return jdbcTemplate.queryForObject("select imageURL from restaurants where restaurantName='"+restaurantName+"'", String.class);
    }

    @Override
    public byte[] getRestaurantPic(String restaurantName) throws Exception {

            return jdbcTemplate.queryForObject("select restaurantImage from restaurants where restaurantName = '" + restaurantName + "'", byte[].class);

    }

    @Override
    public String getPhotoUrl(String dishName) {
        return jdbcTemplate.queryForObject("select photoUrl from dish where dishName='"+dishName+"'", String.class);
    }

    @Override
    public byte[] getDishPic(String dishName) throws Exception {
        return jdbcTemplate.queryForObject("select dishPhoto from dish where dishName = '" + dishName + "'", byte[].class);

    }

    @Override
    public List<Restaurants> getRestaurantsByRating() {
        try {
            String restaurant_details = "select restaurantId,restaurantName,address,openingTime,closingTime,latitude,longitude,deliveryType,overallRating,imageURL,ratings,favourites   from restaurants order by overallRating desc";
            return jdbcTemplate.query(restaurant_details, new BeanPropertyRowMapper<>(Restaurants.class));
        }catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public List<Home> Home() {
        try {
            String restaurant_details = "select restaurantName,address,deliveryType,overallRating,imageURL,ratings,favourites  from restaurants  order by overallRating desc limit ?,? ";
            List<Home> restaurants1=  jdbcTemplate.query(restaurant_details, (rs, rowNum) ->
                    new Home( rs.getString("restaurantName"),rs.getString("address"),rs.getString("deliveryType"),rs.getDouble("overallRating"),rs.getString("imageURL"),rs.getInt("ratings"),rs.getInt("favourites")), lowerLimit, upperLimit);
            lowerLimit = lowerLimit + pages;
            if (restaurants1.size() == 0) {
                lowerLimit = 0;
                return jdbcTemplate.query("select restaurantName,address,deliveryType,overallRating,imageURL,ratings,favourites  from restaurants  order by overallRating desc limit ?,? ", (rs, rowNum) ->
                        new Home( rs.getString("restaurantName"),rs.getString("address"),rs.getString("deliveryType"),rs.getDouble("overallRating"),rs.getString("imageURL"),rs.getInt("ratings"),rs.getInt("favourites")), lowerLimit, upperLimit);
            }
            System.out.println(restaurant_details);
            return restaurants1;
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public MenuResponsePanel getRestaurantDetails(Integer restaurantId)
    {
        MenuResponsePanel menuResponsePanel = jdbcTemplate.queryForObject("select restaurantId,restaurantName,overallRating,ratings,deliveryType,address,imageURL from restaurants where restaurantId = " + restaurantId , new BeanPropertyRowMapper<>(MenuResponsePanel.class));
        List<MenuPanel> menuPanels = new ArrayList<>();
        List<String> categories = jdbcTemplate.queryForList("SELECT distinct dishCategory FROM dish inner join menu on menu.dishId = dish.dishId where restaurantId = ?", String.class,restaurantId);
        for(String dishCategory:categories)
        {
            MenuPanel menuPanel = jdbcTemplate.queryForObject("select count(dish.dishId) as noOfItems from dish inner join menu on menu.dishId = dish.dishId where restaurantId = ? and dishCategory = ? ",new BeanPropertyRowMapper<>(MenuPanel.class),restaurantId,dishCategory);
            menuPanel.setDishCategory(dishCategory);
            List<DishResponse> dishes = jdbcTemplate.query("select dishName,price from dish inner join menu on menu.dishId = dish.dishId and dishCategory = ? and restaurantId = ?",new BeanPropertyRowMapper<>(DishResponse.class),dishCategory,restaurantId);
            menuPanel.setDishResponses(dishes);
            menuPanels.add(menuPanel);
        }
        menuResponsePanel.setMenuPanelList(menuPanels);
        return menuResponsePanel;
    }
// Home
    @Override
    public List<TopDishResponse> viewTopDishes()
    {

        String query="select dish.dishId,dish.dishName, dish.photoUrl from dish inner join item on dish.dishId=item.dishId group by dish.dishId order by count(dish.dishId)desc limit ?,?";
        List<TopDishResponse> dishes=new ArrayList<TopDishResponse>();
        jdbcTemplate.query(query, (rs, rowNum) ->
        {
           TopDishResponse dish=new TopDishResponse();
            dish.setDishId(rs.getInt("dish.dishId"));
            dish.setDishName(rs.getString("dish.dishName"));
            dish.setPhotoUrl(rs.getString("dish.PhotoUrl"));
            int count=jdbcTemplate.queryForObject("select count(restaurantId) from menu where dishId=?",Integer.class,dish.getDishId());
            dish.setCount(count);
            dishes.add(dish);
            return dish;
        },lowerLimit,upperLimit);
        lowerLimit = lowerLimit + pages;
        if(dishes.size()==0){
            lowerLimit=0;
             return jdbcTemplate.query(query, (rs, rowNum) ->
             {
                 TopDishResponse dish=new TopDishResponse();
                 dish.setDishId(rs.getInt("dish.dishId"));
                 dish.setDishName(rs.getString("dish.dishName"));
                 dish.setPhotoUrl(rs.getString("dish.PhotoUrl"));
                 int count=jdbcTemplate.queryForObject("select count(restaurantId) from menu where dishId=?",Integer.class,dish.getDishId());
                 dish.setCount(count);
                 dishes.add(dish);
                 return dish;
             },lowerLimit,upperLimit);
        }
        return dishes;
    }

    @Override
    public String addCardDetails(Card card) {
        int id= getUserIdFromEmail();
        try
        {
          jdbcTemplate.update("insert into card (cardNo,userId,CVV,expiryDate) values(?,?,?,?)",card.getCardNo(),id,card.getCVV(),card.getExpiryDate());
          return"added successfully";
        } catch (Exception e) {
            System.out.println(e);
            return"cannot add";
        }

    }

    @Override
    public List <Favourites>viewFavourites() {
        int id= getUserIdFromEmail();
        try
        {
            return jdbcTemplate.query("select * from favourite where userId =?",new BeanPropertyRowMapper<>(Favourites.class),id);

        }catch (Exception e) {
            System.out.println(e);
            return  null;
        }
    }


    public String getUserNameFromToken()
    {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails)
        {
            username = ((UserDetails) principal).getUsername();
        }
        else
        {
            username = principal.toString();
        }
        return username;
    }


    public int getUserIdFromEmail()
    {
        String email = getUserNameFromToken();
        int userId = jdbcTemplate.queryForObject("select userId from user where emailId=?",new Object[]{email},Integer.class);
        return userId;
    }
//    @Override
//    public String addToCart(CartInfo cartInfo)
//    {
//        int id=getUserIdFromEmail();
//        double price = jdbcTemplate.queryForObject("select price from menu where restaurantId =? and dishId =?",Double.class,cartInfo.getRestaurantId(),cartInfo.getDishId());
//        double TotalAmount = price*cartInfo.getQuantity();
//        jdbcTemplate.update("insert into cart (userId, restaurantId, totalAmount) values(?,?,?)",id,cartInfo.getRestaurantId(),TotalAmount);
//        int cartId=jdbcTemplate.queryForObject("select max(cartId) from cart where userId=?", Integer.class, new Object[]{id});
//        jdbcTemplate.update("insert into item (dishId, cartId, quantity) values(?,?,?)",cartInfo.getDishId(),cartId,cartInfo.getQuantity());
//        return "Cart added Successfully";
//    }

    @Override
    public  String addToCart(Cart cart)
    {
        String query="insert into cart(userId,restaurantId,totalAmount) values(?,?,?)";
        jdbcTemplate.update(query,cart.getUserId(),cart.getRestaurantId(),cart.getTotalAmount());
        return "cart added successfully";
    }

    public int getRecentlyCreatedCartId(int id)
    {
        return jdbcTemplate.queryForObject("select max(cartId) from cart where userId="+id, Integer.class);
    }

    public double getTotalPrice(CartResponse cartResponse) {
        double totalPrice = 0;
        for (Item item : cartResponse.getItems()) {
            System.out.println(item.getDishId());
            double price = jdbcTemplate.queryForObject("select price from menu where dishId =? and restaurantId=?", Double.class , item.getDishId(),cartResponse.getRestaurantId());
            System.out.println(price);

            totalPrice += (price * item.getQuantity());
        }
        return totalPrice;
    }
//    @Override

public String addCartItems(List<Item> items,int cartId)
{
    String query="insert into item(dishId,quantity,cartId) values(?,?,?)";
    for(Item item:items)
    {
        jdbcTemplate.update(query,item.getDishId(),item.getQuantity(),cartId);
    }
    return "items added successfully";
}
//    @Override
    public int addToOrder(CartResponse cartResponse)
    {
        int id = getUserIdFromEmail();
        Cart cart=new Cart();
        cart.setUserId(id);
        cart.setRestaurantId(cartResponse.getRestaurantId());
        cart.setTotalAmount(this.getTotalPrice(cartResponse));
        addToCart(cart);
        int cartId=this.getRecentlyCreatedCartId(id);
        this.addCartItems(cartResponse.getItems(),cartId);
        return cartId;

    }

    @Override
    public List<Cart> viewCart()
    {
        int id = getUserIdFromEmail();
        String query="select * from cart where userId="+id;
        return jdbcTemplate.query(query,new BeanPropertyRowMapper<>(Cart.class));
    }
  @Override
    public List<Item> viewItems()
    {
        int id = getUserIdFromEmail();
        String query= "select cartId from cart where userId="+id;
        List<Integer> cartIds = jdbcTemplate.queryForList(query,Integer.class);
        List<Item> list = new ArrayList<>();
        for (int cartId : cartIds) {
            System.out.println(cartId);
            String query1 = "select * from item where cartId=" + cartId;
            list.addAll(jdbcTemplate.query(query1, new BeanPropertyRowMapper<>(Item.class)));
        }
        return list;
    }


    @Override
    public String placeOrder(Orders orders)
    {
        try {
            jdbcTemplate.queryForObject("select cartId from orders where cartId = ?",Integer.class,orders.getCartId());
            return "Order already Placed";
        }
        catch (Exception e) {
            try {

                int id = getUserIdFromEmail();
                int cartNo=jdbcTemplate.queryForObject("select max(cartId)from cart where userId =?",Integer.class,id);
                Card card = jdbcTemplate.queryForObject("select * from card where cardNo = ? and userId=?", new BeanPropertyRowMapper<>(Card.class), orders.getCardNo(),id);
//
                if(cartNo==orders.getCartId()) {
                    System.out.println(orders.getCvv() + "" + card.getCVV());
                    System.out.println(orders.getExpiryDate() + "" + card.getExpiryDate());
                    if (orders.getCvv() == card.getCVV()) {
                        jdbcTemplate.queryForObject("select * from card where expiryDate = ? and cardNo = ?", new BeanPropertyRowMapper<>(Card.class), orders.getExpiryDate(), orders.getCardNo());
                        String query = "insert into orders(cartId,cardNo,deliveryAddress,orderStatus) values(?,?,?,?)";
                        jdbcTemplate.update(query, orders.getCartId(), orders.getCardNo(), orders.getDeliveryAddress(), "success");
                        return "order placed successfully";
                    }
                    return "Invalid Card Details";


                }
                return "invalid cartId";
            } catch (Exception exception) {
                e.printStackTrace();
                return "Invalid Card Details";
            }
        }
    }
}



