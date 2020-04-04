package com.reservation.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reservation.models.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	
	Optional<Restaurant> findRestaurantByName(String name);
	
	Page<Restaurant> findAll(Pageable pageable);
	
	Page<Restaurant> findAllByName(String query, Pageable pageable);

}
