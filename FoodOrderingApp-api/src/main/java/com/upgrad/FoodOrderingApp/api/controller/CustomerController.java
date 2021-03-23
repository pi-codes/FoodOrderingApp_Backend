package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CustomerController {

@RequestMapping(method = RequestMethod.POST, path = "/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public ResponseEntity<SignupCustomerResponse> signup(final SignupCustomerRequest signupCustomerRequest)
{
    return null;
}

@RequestMapping(method = RequestMethod.POST, path = "/customer/login", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public ResponseEntity<LoginResponse> login(@RequestHeader("authorization")final String authorization )
{

    return null;
}

@RequestMapping(method = RequestMethod.POST, path = "/customer/logout", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public ResponseEntity<LogoutResponse> logout(@RequestHeader("authorization")final String authorization)
{

    return null;
}

@RequestMapping(method = RequestMethod.PUT, path = "/customer", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public ResponseEntity<UpdateCustomerResponse> updateCustomer(@RequestHeader("authorization")final String authorization, final UpdateCustomerRequest updateCustomerRequest)
{
    return null;
}

public ResponseEntity<UpdatePasswordRequest> updatePassword(@RequestHeader("authorization")final String authorization, final UpdatePasswordResponse updatePasswordResponse)
{
    return null;
}

}
