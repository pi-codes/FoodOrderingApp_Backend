package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
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

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity signup(CustomerEntity customerEntity) throws SignUpRestrictedException
    {
        CustomerEntity existingCustomer = customerDao.findByContactNumber(customerEntity.getContact_number());

        if(existingCustomer != null) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number");
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
    public CustomerAuthEntity logout(final String authorization) throws AuthenticationFailedException
    {
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerByAuthToken(authorization);

        if(customerAuthEntity == null) {
            throw new AuthenticationFailedException("ATHR-001", "Customer is not Logged in.");
        }
        if(customerAuthEntity.getLogoutAt() != null) {
            throw new AuthenticationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }

        final ZonedDateTime now = ZonedDateTime.now();

        if(customerAuthEntity.getExpiresAt().compareTo(now) < 0) {
            throw new AuthenticationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

        customerAuthEntity.setLogoutAt(ZonedDateTime.now());

        CustomerAuthEntity updatedCustomerAuthEntity = customerAuthDao.signout(customerAuthEntity);

        return updatedCustomerAuthEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(final CustomerEntity customerEntity, final String authorization) throws UpdateCustomerException, AuthenticationFailedException
    {
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerByAuthToken(authorization);

        if(customerEntity.getFirstname() == null) {
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }

        if(customerAuthEntity == null) {
            throw new AuthenticationFailedException("ATHR-001", "Customer is not Logged in.");
        }
        if(customerAuthEntity.getLogoutAt() != null) {
            throw new AuthenticationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }
        final ZonedDateTime now = ZonedDateTime.now();

        if(customerAuthEntity.getExpiresAt().compareTo(now) < 0) {
            throw new AuthenticationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

        CustomerEntity customerToUpdateEntity = customerDao.findByUuid(customerEntity.getUuid());
        customerToUpdateEntity.setFirstname(customerEntity.getFirstname());
        customerToUpdateEntity.setLastname(customerEntity.getLastname());

        CustomerEntity updateCustomer = customerDao.updateCustomer(customerEntity);
        return updateCustomer;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updatePassword(final CustomerEntity customerEntity, final String existingPassword, final String newPassword) throws UpdateCustomerException
    {
        String encryptedExistingPassword = passwordCryptographyProvider.encrypt(existingPassword, customerEntity.getSalt());
        return null;
    }


}
