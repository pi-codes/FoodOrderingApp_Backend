package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantEntity> restaurantsByRating() {
        return null;
    }

    public List<RestaurantEntity> restaurantsByName(String name) {
        return null;
    }

    public RestaurantEntity restaurantsById(String restaurantId) {
        return null;
    }

    public List<RestaurantEntity> restaurantsByCategory(CategoryEntity categoryEntity) {
        return null;
    }

    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity)
    {
        return null;
    }

}
