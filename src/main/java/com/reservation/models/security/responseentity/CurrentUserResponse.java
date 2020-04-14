package com.reservation.models.security.responseentity;

import com.reservation.models.Restaurant;

public class CurrentUserResponse {

    private Long id;
    private String name;
    private String email;
    private String imageUrl;
    private Restaurant restaurant;


    public CurrentUserResponse(Long id, String name, String email, String imageUrl, Restaurant restaurant) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.restaurant = restaurant;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}
