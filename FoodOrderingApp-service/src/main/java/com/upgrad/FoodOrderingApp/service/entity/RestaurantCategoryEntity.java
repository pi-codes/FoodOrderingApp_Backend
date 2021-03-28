package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

//This Class represents the RestaurantCategory table in the DB

@Entity
@Table(name = "restaurant_category")
@NamedQueries({

        @NamedQuery(name = "getCategoriesByRestaurant",query = "SELECT r FROM RestaurantCategoryEntity r WHERE r.restaurantEntity= :restaurant ORDER BY r.categoryEntity.categoryName ASC "),
        @NamedQuery(name = "getRestaurantsByCategory",query = "SELECT r FROM RestaurantCategoryEntity r WHERE r.categoryEntity = :category ORDER BY r.restaurantEntity.customerRating DESC "),
})

public class RestaurantCategoryEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurantEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RestaurantEntity getRestaurantEntity() {
        return restaurantEntity;
    }

    public void setRestaurantEntity(RestaurantEntity restaurantEntity) {
        this.restaurantEntity = restaurantEntity;
    }

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }
}
