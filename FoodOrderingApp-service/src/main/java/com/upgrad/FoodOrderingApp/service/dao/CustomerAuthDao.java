package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerAuthDao {

    @PersistenceContext
    private EntityManager entityManager;

    public CustomerEntity getAuthToken(final String authorization)
    {
        return null;
    }



    public CustomerEntity signout(final CustomerEntity userAuthTokenEntity)
    {
        return null;
    }
}
