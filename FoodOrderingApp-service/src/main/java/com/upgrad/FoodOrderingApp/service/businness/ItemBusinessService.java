package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemBusinessService {

    public List<ItemEntity> getItemByPopularity(RestaurantEntity restaurantEntity)
    {
        return null;
    }

    public List<OrderEntity> getMostPopularItems(String restaurantId)
    {
        return null;
    }
}
