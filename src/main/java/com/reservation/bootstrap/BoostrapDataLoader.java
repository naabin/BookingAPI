package com.reservation.bootstrap;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.reservation.models.security.BookingUser;
import com.reservation.models.security.Role;
import com.reservation.models.security.UserRole;
import com.reservation.services.BookingUserService;
import com.reservation.services.ReservationService;

@Component
public class BoostrapDataLoader implements CommandLineRunner {

//	private  ReservationService reservationService;

	private final BookingUserService bookingUserService;


	public BoostrapDataLoader(
			ReservationService reservationService, 
			BookingUserService bookingUserService
			) {

//		this.reservationService = reservationService;
		this.bookingUserService = bookingUserService;

	}

	@Override
	public void run(String... args) throws Exception {
//		Reservation reservation = new Reservation();
//		reservation.setId(1L);
//		reservation.setFullName("Nabin Karki");
//		reservation.setEmail("naabin@outlook.com");
//		reservation.setNumberOfPeople(2);
//		reservation.setPhoneNumber("012345678");
//		reservation.setDate(LocalDate.now());
//		reservation.setTime(LocalDateTime.now());
//		reservation.setSpecialRequest("I would like some food without any meat on it.");
//
//		this.reservationService.createReservation(reservation);
//
//		Reservation reservation2 = new Reservation();
//		reservation2.setId(2L);
//		reservation2.setFullName("Thomas Wayne");
//		reservation2.setEmail("thomas@batman.com");
//		reservation2.setDate(LocalDate.now());
//		reservation2.setTime(LocalDateTime.now());
//		reservation2.setPhoneNumber("012345678");
//		reservation2.setSpecialRequest("I would like some meat of bats.");
//		reservation2.setNumberOfPeople(1);
//
//		this.reservationService.createReservation(reservation2);

		BookingUser user = new BookingUser();
		user.setFirstName("Nabin");
		user.setLastName("Karki");
		user.setEmail("nabinkarki80@gmail.com");
		user.setUsername("Nabin");
		user.setPassword("test");

		Set<UserRole> userRoles = new HashSet<UserRole>();
		Role role1 = new Role();
		role1.setRole("ROLE_ADMIN");
		
		Role role2 = new Role();
		role2.setRole("ROLE_USER");
		

		userRoles.add(new UserRole(user, role1));
		userRoles.add(new UserRole(user, role2));

		this.bookingUserService.createUser(user, userRoles);

		
		
		userRoles.clear();
		
		
//		Restaurant restaurant = new Restaurant();
//		restaurant.setId(1l);
//		restaurant.setAddress("Sydney, Australia");
//		restaurant.setName("Java Restaurant");
//		restaurant.setPhoneNumber("1234567890");
//		restaurant.setCreatedBy("Nabin");
//		restaurant.setCreatedDate(LocalDateTime.now());
//		restaurant.setLastModifiedBy("Nabin");
//		restaurant.setLastModifiedDate(LocalDateTime.now());
//		
//		Set<OpeningHours> openingHours = new HashSet<OpeningHours>();
//		
//		OpeningHours sunday = new OpeningHours();
//		sunday.setDayOfWeek("Monday");
//		sunday.setId(1L);
//		sunday.setFrom(LocalTime.now());
//		sunday.setTo(LocalTime.MIDNIGHT);
//		
//		openingHours.add(sunday);
//		
//		this.restaurantService.createRestaurant(restaurant, openingHours);	
		
		System.out.println("Loading dummy data via bootstrap loader...");
		
//		openingHours.clear();

	}

}
