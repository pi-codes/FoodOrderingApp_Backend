package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerAuthDao {

    @PersistenceContext
    private EntityManager entityManager;

    public CustomerAuthEntity createCustomerAuthEntity(CustomerAuthEntity customerAuthEntity) {
        entityManager.persist(customerAuthEntity);
        return customerAuthEntity;
    }

    public CustomerAuthEntity getCustomerByAuthToken(final String authorization)
    {
        try {
            CustomerAuthEntity customerAuthEntity = entityManager.createNamedQuery("getCustomerByAuthToken", CustomerAuthEntity.class).setParameter("accessToken", authorization).getSingleResult();
            return customerAuthEntity;
        }catch (NoResultException nre) {
            return null;
        }
    }

    public String getAuthTokenByUuid(String uuid) {
        try {
            CustomerAuthEntity customerAuthEntity = entityManager.createNamedQuery("getCustomerByUuid", CustomerAuthEntity.class).setParameter("uuid", uuid).getSingleResult();
            return customerAuthEntity.getAccessToken();
        }catch (NoResultException nre) {
            return null;
        }
    }



    public CustomerAuthEntity signout(final CustomerAuthEntity customerAuthEntity)
    {
        entityManager.merge(customerAuthEntity);
        return customerAuthEntity;
    }
}
