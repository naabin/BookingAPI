package com.reservation.services.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.reservation.models.OpeningHours;
import com.reservation.models.Restaurant;
import com.reservation.models.security.User;
import com.reservation.repositories.OpeningHoursRepository;
import com.reservation.repositories.RestaurantRepository;
import com.reservation.services.UserService;
import com.reservation.services.RestaurantService;

@Service
public class RestaurantServiceImpl implements RestaurantService {

	private final RestaurantRepository restaurantRepository;
	private final OpeningHoursRepository openingHoursRepository;
	private final UserService userService;

	public RestaurantServiceImpl(RestaurantRepository restaurantRepository,
			OpeningHoursRepository openingHoursRepository, UserService userService) {
		this.restaurantRepository = restaurantRepository;
		this.openingHoursRepository = openingHoursRepository;
		this.userService = userService;
	}

	@Override
	@Transactional
	public Restaurant createRestaurant(Restaurant restaurant, List<OpeningHours> openingHours) {
		for (OpeningHours openingHour : openingHours) {
			openingHour.setRestaurantOpeningHours(restaurant);

			this.openingHoursRepository.save(openingHour);
		}
		if (restaurant.getUser() != null) {
			Optional<User> optionalUser = this.userService.findUserById(restaurant.getUser().getId());
			if (optionalUser.isPresent()) {
				User bookingUser = optionalUser.get();
				bookingUser.setRestaurant(restaurant);
				this.userService.updateUser(bookingUser);
			}

		}
		restaurant.getOpeningHours().addAll(openingHours);
		return this.restaurantRepository.save(restaurant);
	}

	@Override
	public Restaurant updateRestaurant(Restaurant restaurant) {
		List<OpeningHours> openingHours = restaurant.getOpeningHours();
		if (!openingHours.isEmpty()) {
			for (OpeningHours openingHour : openingHours) {
				this.openingHoursRepository.saveAndFlush(openingHour);
			}
		}

		restaurant.getOpeningHours().addAll(openingHours);
		return this.restaurantRepository.saveAndFlush(restaurant);
	}

	@Override
	public Page<Restaurant> getAllRestaurants(Integer pageNumber, Integer pageSize, String restaurantName) {
		PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
		if (restaurantName.length() > 1) {
			return this.restaurantRepository.findByNameContaining(restaurantName, pageRequest);
		} else {
			Page<Restaurant> restuarant = this.restaurantRepository.findAll(pageRequest);
			return restuarant;
		}

	}

	@Override
	public Optional<Restaurant> findRestaurantById(Long id) {

		return this.restaurantRepository.findById(id);
	}

	@Override
	public void deleteRestaurantById(Long id) {
		Optional<Restaurant> restaurant = this.restaurantRepository.findById(id);
		if (restaurant.isPresent()) {
			this.restaurantRepository.deleteById(id);
		}
	}

	@Override
	public void deleteRestaurant(Restaurant restaurant) {

		if (restaurant != null) {
			this.restaurantRepository.delete(restaurant);
		}

	}

	@Override
	public Optional<Restaurant> findRestaurant(String restaurantName) {
		return this.restaurantRepository.findRestaurantByName(restaurantName);
	}

}
