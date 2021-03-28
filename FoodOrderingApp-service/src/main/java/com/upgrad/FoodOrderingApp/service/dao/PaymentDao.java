package com.upgrad.FoodOrderingApp.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class PaymentDao {
    @PersistenceContext
    private EntityManager entityManager;
}
