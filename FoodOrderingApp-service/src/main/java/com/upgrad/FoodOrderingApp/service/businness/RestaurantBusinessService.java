package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.common.Utility;
import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;


//This Class handles all service related to the Restaurant.

@Service
public class RestaurantBusinessService {

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    RestaurantCategoryDao restaurantCategoryDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    Utility utility;

    public List<RestaurantEntity> restaurantsByRating(){

        List<RestaurantEntity> restaurantEntities = restaurantDao.restaurantsByRating();
        return restaurantEntities;
    }

    public List<RestaurantEntity> restaurantsByName(String restaurantName)throws RestaurantNotFoundException{
        if(restaurantName == null || restaurantName ==""){
            throw new RestaurantNotFoundException("RNF-003","Restaurant name field should not be empty");
        }


        List<RestaurantEntity> restaurantEntities = restaurantDao.restaurantsByName(restaurantName);
        return restaurantEntities;
    }

    public List<RestaurantEntity> restaurantByCategory(String categoryId) throws CategoryNotFoundException {

        if(categoryId == null || categoryId == ""){
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }

        CategoryEntity categoryEntity = categoryDao.getCategoryById(categoryId);

        if(categoryEntity == null){
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        }

        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDao.getRestaurantByCategory(categoryEntity);

        List<RestaurantEntity> restaurantEntities = new LinkedList<>();
        restaurantCategoryEntities.forEach(restaurantCategoryEntity -> {
            restaurantEntities.add(restaurantCategoryEntity.getRestaurantEntity());
        });
        return restaurantEntities;
    }

    public RestaurantEntity restaurantByuuid(String uuid)throws RestaurantNotFoundException{
        if(uuid == null||uuid == ""){
            throw new RestaurantNotFoundException("RNF-002","Restaurant id field should not be empty");
        }

        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(uuid);

        if (restaurantEntity == null){
            throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
        }

        return restaurantEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity, Double customerRating) throws InvalidRatingException {
        if(!utility.isValidCustomerRating(customerRating.toString())){ //Checking for the rating to be valid
            throw new InvalidRatingException("IRE-001","Restaurant should be in the range of 1 to 5");
        }
        //Finding the new Customer rating adn updating it.
        DecimalFormat format = new DecimalFormat("##.0");
        double restaurantRating = restaurantEntity.getCustomerRating();
        Integer restaurantNoOfCustomerRated = restaurantEntity.getNumber_of_customers_rated();
        restaurantEntity.setNumber_of_customers_rated(restaurantNoOfCustomerRated+1);

        //calculating the new customer rating as per the given data and formula
        double newCustomerRating = (restaurantRating*(restaurantNoOfCustomerRated.doubleValue())+customerRating)/restaurantEntity.getNumber_of_customers_rated();

        restaurantEntity.setCustomerRating(Double.parseDouble(format.format(newCustomerRating)));

        //Updating the restautant in the db using the method updateRestaurantRating of restaurantDao.
        RestaurantEntity updatedRestaurantEntity = restaurantDao.updateRestaurantRating(restaurantEntity);

        return updatedRestaurantEntity;

    }
}
