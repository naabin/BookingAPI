package com.reservation.bootstrap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.reservation.models.security.User;
import com.reservation.models.security.AuthProvider;
import com.reservation.services.UserService;
import com.reservation.services.ReservationService;

@Component
public class BoostrapDataLoader implements CommandLineRunner {


	private final UserService userService;


	public BoostrapDataLoader(
			ReservationService reservationService, 
			UserService userService
			) {

		this.userService = userService;

	}

	@Override
	public void run(String... args) throws Exception {

//		User user = new User();
//		user.setEmail("nabinkarki80@gmail.com");
//		user.setName("Admin");
//		user.setPassword("test");
//		user.setProvider(AuthProvider.local);
//		this.userService.createUser(user);
//		System.out.println("Loading dummy data via bootstrap loader...");

	}

}
