package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.common.Utility;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class CustomerBusinessService {

    @Autowired
    CustomerDao customerDao;

    @Autowired
    PasswordCryptographyProvider passwordCryptographyProvider;

    @Autowired
    CustomerAuthDao customerAuthDao;

    @Autowired
    Utility utility;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity signup(CustomerEntity customerEntity) throws SignUpRestrictedException
    {
        CustomerEntity existingCustomer = customerDao.findByContactNumber(customerEntity.getContact_number());

        if(existingCustomer != null) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
        }

        if(!utility.isValidSignupRequest(customerEntity)) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
        }

        if(!utility.isValidEmail(customerEntity.getEmail())) {
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
        }

        if(!utility.isValidContact(customerEntity.getContact_number())) {
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
        }

        if(!utility.isValidPassword(customerEntity.getPassword())) {
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
        }

        String[] encryptedPassword = passwordCryptographyProvider.encrypt(customerEntity.getPassword());
        customerEntity.setSalt(encryptedPassword[0]);
        customerEntity.setPassword(encryptedPassword[1]);

        CustomerEntity createdCustomerEnity = customerDao.customerSignup(customerEntity);

        return createdCustomerEnity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity login(String contactNumber, String password) throws AuthenticationFailedException
    {
        CustomerEntity customerEntity = customerDao.findByContactNumber(contactNumber);

        if(customerEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }

        String encryptedPassword = passwordCryptographyProvider.encrypt(password, customerEntity.getSalt());

        if(encryptedPassword.equals(customerEntity.getPassword())) {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
            customerAuthEntity.setCustomer(customerEntity);

            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(4);

            customerAuthEntity.setAccessToken(jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt));
            customerAuthEntity.setLoginAt(now);
            customerAuthEntity.setExpiresAt(expiresAt);
            customerAuthEntity.setUuid(UUID.randomUUID().toString());

            //not necessary to persist as shown below, TBD. Can directly return customerAuthEntity
            CustomerAuthEntity createdCustomerAuthEntity = customerAuthDao.createCustomerAuthEntity(customerAuthEntity);

            return createdCustomerAuthEntity;
        }
        else {
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity logout(final String authorization) throws AuthorizationFailedException
    {
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerByAuthToken(authorization);

        if(customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }
        if(customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }

        final ZonedDateTime now = ZonedDateTime.now();

        if(customerAuthEntity.getExpiresAt().compareTo(now) < 0) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

        customerAuthEntity.setLogoutAt(ZonedDateTime.now());

        CustomerAuthEntity updatedCustomerAuthEntity = customerAuthDao.signout(customerAuthEntity);

        return updatedCustomerAuthEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(final CustomerEntity customerEntity, final String accessToken) throws UpdateCustomerException, AuthorizationFailedException
    {
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerByAuthToken(accessToken);

        if(customerEntity.getFirstname() == null || customerEntity.getFirstname() == "") {
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }

        if(customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }
        if(customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }
        final ZonedDateTime now = ZonedDateTime.now();

        if(customerAuthEntity.getExpiresAt().compareTo(now) < 0) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

        CustomerEntity customerToUpdateEntity = customerDao.findByUuid(customerEntity.getUuid());
        customerToUpdateEntity.setFirstname(customerEntity.getFirstname());
        customerToUpdateEntity.setLastname(customerEntity.getLastname());

        CustomerEntity updateCustomer = customerDao.updateCustomer(customerEntity);
        return updateCustomer;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updatePassword(final String authorization, final CustomerEntity customerEntity, final String oldPassword, final String newPassword) throws UpdateCustomerException, AuthorizationFailedException
    {
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerByAuthToken(authorization);

        if(oldPassword.equals("") || newPassword.equals("")) {
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }

        if(customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }

        if(customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }

        final ZonedDateTime now = ZonedDateTime.now();

        if(customerAuthEntity.getExpiresAt().compareTo(now) < 0) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

        if(!utility.isValidPassword(newPassword)) {
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }

        String encryptedExistingPassword = passwordCryptographyProvider.encrypt(oldPassword, customerEntity.getSalt());

        if(encryptedExistingPassword.equals(customerEntity.getPassword())) {
            CustomerEntity toBeUpdatedCustomerEntity = customerDao.findByUuid(customerEntity.getUuid());

            String[] encryptedPassword = passwordCryptographyProvider.encrypt(newPassword);
            toBeUpdatedCustomerEntity.setSalt(encryptedPassword[0]);
            toBeUpdatedCustomerEntity.setPassword(encryptedPassword[1]);

            CustomerEntity updatedCustomerEntity = customerDao.updateCustomer(toBeUpdatedCustomerEntity);

            return updatedCustomerEntity;
        }
        else {
            throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
        }
    }

    public CustomerEntity getCustomer(String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerByAuthToken(accessToken);

        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }

        if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }

        final ZonedDateTime now = ZonedDateTime.now();

        if (customerAuthEntity.getExpiresAt().compareTo(now) <= 0) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
        return customerAuthEntity.getCustomer();
    }


}
