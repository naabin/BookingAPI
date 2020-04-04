package com.reservation.controllers.publiccontrollers;

import java.time.OffsetDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reservation.exception.ResourceNotFoundException;
import com.reservation.models.Reservation;
import com.reservation.models.Restaurant;
import com.reservation.services.EmailService;
import com.reservation.services.ReservationService;
import com.reservation.services.RestaurantService;

@RestController
@RequestMapping("/api/public/reservation")
public class PublicReservationController {
	
	private final RestaurantService restaurantService;
	private final ReservationService reservationService;
	private final EmailService emailService;
	
	public PublicReservationController(
			RestaurantService restaurantService,
			ReservationService reservationService,
			EmailService emailService) {
		this.restaurantService = restaurantService;
		this.reservationService = reservationService;
		this.emailService = emailService;
	}
	
	@PostMapping
	public ResponseEntity<?> createReservation(
			@RequestParam("restaurant") String restaurnatName,
			@RequestBody Reservation reservation) throws ResourceNotFoundException{
		Restaurant restaurant = this.restaurantService.findRestaurant(restaurnatName)
			.orElseThrow(() -> new ResourceNotFoundException("Restaurant could not be founds"));
		reservation.setRestaurant(restaurant);
		reservation.setCreatedBy(reservation.getFullName());
		reservation.setCreatedDate(OffsetDateTime.now());
		reservation.setRestaurant(restaurant);
		this.reservationService.createReservation(reservation);
		String subject = "Reservation request from " + reservation.getFullName();
		String body = "Name: " + reservation.getFullName() + "\n"+
					  "Date: " + reservation.getDate() + "\n"+
					  "Time: " + reservation.getTime() + "\n"+
					  "Number of People" + reservation.getNumberOfPeople()+"\n"+
					  "Specail request" + reservation.getSpecialRequest();
		String link = "http://localhost:4200/reservation/confirm/"+reservation.getId();
		this.emailService.sendHtml(reservation.getEmail(), restaurant.getEmail(), subject, body, link);
		return ResponseEntity.ok().build();
		
	}
}
