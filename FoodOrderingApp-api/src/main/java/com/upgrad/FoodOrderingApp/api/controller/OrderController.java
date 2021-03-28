package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.OrderList;
import com.upgrad.FoodOrderingApp.api.model.SaveOrderRequest;
import com.upgrad.FoodOrderingApp.api.model.SaveOrderResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Controller
public class OrderController {

    @RequestMapping(method = RequestMethod.GET, value = "/order/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCouponByName(@PathVariable("coupon_name")final String coupon_name, @RequestHeader("authorization")final String authorization)
    {
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/order", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> postOrder(@RequestBody SaveOrderRequest saveOrderRequest,@RequestHeader("authorization")final String authorization )
    {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<OrderList>> retriveAllOrder(@RequestHeader("authorization")final String authorization )
    {
        return null;
    }
}
