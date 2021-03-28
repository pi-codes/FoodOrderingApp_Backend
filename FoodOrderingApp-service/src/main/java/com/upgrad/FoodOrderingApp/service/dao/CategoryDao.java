package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<CategoryEntity> getAllCategories(){
        return null;
    }

    public CategoryEntity getCategoryById(final String categryId)
    {
        return null;
    }

    public List<CategoryEntity> getCategoriesByRestaurant(RestaurantEntity restaurantEntity)
    {
        return null;
    }
}
