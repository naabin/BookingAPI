package com.reservation.controllers.publiccontrollers;

import java.net.MalformedURLException;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reservation.exception.ResourceNotFoundException;
import com.reservation.models.Restaurant;
import com.reservation.services.RestaurantService;

@RestController
@RequestMapping("/api/public/restaurant")
public class PublicRestaurantController {
	
	private final RestaurantService restaurantService;
	
	public PublicRestaurantController(RestaurantService restaurantService) {
		this.restaurantService = restaurantService;
	}
	
	@GetMapping
	public ResponseEntity<?> getRestaurants(
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(name = "search", required = false) String searchQuery
			) throws MalformedURLException{
		Page<Restaurant> allRestaurants = this.restaurantService.getAllRestaurants(pageNumber, pageSize, searchQuery);
		allRestaurants.forEach(res -> {
			res.setUser(null);
			res.setBookings(null);
		});
		return ResponseEntity.ok().body(allRestaurants);
	}
	
	@GetMapping("/{name}")
	public ResponseEntity<Restaurant> getRestaurantByName(@PathVariable("name")String name) throws ResourceNotFoundException{
		Restaurant restaurant = this.restaurantService.findRestaurant(name).orElseThrow(() -> new ResourceNotFoundException("Could not locate the resource"));
		restaurant.setUser(null);
		restaurant.setBookings(null);
		return ResponseEntity.ok().body(restaurant);
	}
}
