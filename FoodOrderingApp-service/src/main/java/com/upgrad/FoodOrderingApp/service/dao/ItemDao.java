package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    public ItemEntity getItemById(String uuid)
    {
        return null;
    }

    public List<OrderEntity> getOrdersByRestaurant(RestaurantEntity restaurantEntity)
    {
        return null;
    }

    public List<ItemEntity> getPopularOrders(RestaurantEntity restaurantEntity)
    {
        return null;
    }


}
