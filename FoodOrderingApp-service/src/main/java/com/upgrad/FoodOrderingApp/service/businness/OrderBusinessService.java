package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderBusinessService {

    public CouponEntity getCouponDetailsByName(final String name)
    {
        return null;
    }

    public OrderEntity postOrder(OrderEntity orderEntity, String restaurantId, String PaymentId,String couponId,String addressId,String authorization)
    {
        return null;
    }

    public CouponEntity getCouponDetailsById(String id)
    {
        return null;
    }

    public OrderItemEntity postOrder(OrderItemEntity orderItemEntity)
    {
        return null;
    }

    public List<OrderEntity> retriveAllOrders(CustomerEntity customerEntity, String accessToken)
    {
        return null;
    }


    public OrderItemEntity fetchItemDetails(OrderEntity ordeEntity)
    {
        return null;
    }
}
