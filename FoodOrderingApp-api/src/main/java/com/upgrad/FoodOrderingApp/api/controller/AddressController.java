package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AddressController {

    @RequestMapping(method = RequestMethod.POST, path = "/address", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestHeader("authorization")final String authorization, final SaveAddressRequest saveAddressRequest)
    {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/address/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressList> getAllSavedAddress(@RequestHeader("authorization")final String authorization)
    {
        return null;
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/address/{address_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteSavedAddress(@RequestHeader("authorization")final String authorization, @PathVariable("address_id")final String addressId)
    {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/state", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAllSavedAddress()
    {
        return null;
    }

}
