package com.reservation.controllers;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reservation.exception.ResourceNotFoundException;
import com.reservation.models.security.AuthProvider;
import com.reservation.models.security.User;
import com.reservation.models.security.UserAvailability;
import com.reservation.securityconfig.PasswordEncrypt;
import com.reservation.services.UserService;
import com.reservation.services.EmailService;
import com.reservation.models.security.responseentity.CurrentUserResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/user")
@Api(value = "User Creation and logout APIs")
public class UserController {

	private final UserService userService;

	private final EmailService emailService;

	private final PasswordEncrypt passwordEncrypt;

	public UserController(UserService userService, EmailService emailService, PasswordEncrypt passwordEncrypt) {

		this.userService = userService;
		this.emailService = emailService;
		this.passwordEncrypt = passwordEncrypt;

	}

	@PostMapping
	@ApiOperation(value = "Controller for user registration")
	public ResponseEntity<?> userRegistration(
			@ApiParam(value = "User object to be stored in the database", required = true) @Valid @RequestBody User user)
			throws Exception {
		try {
			user.setProvider(AuthProvider.local);
			this.userService.createUser(user);
			String html = "User with " + user.getName() + " has been successfully created.";
			emailService.sendHtml("naabin@outlook.com", user.getEmail(), "User Registration", html, null);
			return ResponseEntity.ok().body(user);
		} catch (Exception e) {
			throw new Exception("Internal sever error.");
		}

	}

	@PostMapping("/sendtoken")
	public ResponseEntity<?> resetPassword(@RequestParam(name = "email", required = true) String email) {
		User user = this.userService.loadUserByEmail(email);
		if (user != null) {
			SecureRandom random = new SecureRandom();
			int randomInt = random.nextInt(1000000);
			user.setResetPin(randomInt);
			this.userService.updateUser(user);
			String html = "Password reset token is " + randomInt;
			this.emailService.sendHtml("naabin@outlook.com", email, "Password reset token", html, null);
			return ResponseEntity.ok().body(new HashMap<String, Boolean>().put("email", true));
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/validatetoken")
	public ResponseEntity<?> validateToken(@RequestParam("resetToken") String resetToken)
			throws ResourceNotFoundException {
		Integer token = Integer.parseInt(resetToken);
		User user = this.userService.findUserByResetPin(token)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid token"));

		user.setResetPin(null);
		this.userService.updateUser(user);
		return ResponseEntity.ok().body(new HashMap<String, Boolean>().put("userExists", true));

	}

	@PostMapping("/resetpassword")
	public ResponseEntity<?> resetPassword(@RequestParam("email") String email,
			@RequestParam("password") String password) {
		User bookingUser = this.userService.loadUserByEmail(email);
		if (bookingUser != null) {
			bookingUser.setPassword(this.passwordEncrypt.passwordEncoder().encode(password));
			this.userService.updateUser(bookingUser);
			return ResponseEntity.ok().body(new HashMap<String, Boolean>().put("passwordChanged", true));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@GetMapping
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Succefully retrieved lists of registered users"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resouce."),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to access is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to find is either unavailabe or not found.") })
	@ApiOperation(value = "View all the list of registered users", response = List.class)
	public ResponseEntity<List<User>> getAllRegisteredUsers() {
		List<User> allUsers = userService.getAllUsers();

		return ResponseEntity.ok().body(allUsers);
	}

	@PostMapping("/checkuniqueuser")
	public ResponseEntity<?> checkUniqueUserAvailability(
			@RequestParam(required = true, name = "username") String username) {
		boolean userAvailable = this.userService.uniqueUserAvailable(username.toLowerCase());
		HashMap<String, Boolean> available = new HashMap<String, Boolean>();
		available.put("available", false);
		UserAvailability userAvailability = new UserAvailability(available);
		if (userAvailable) {
			available.put("available", true);
			return ResponseEntity.ok().body(userAvailability);
		}
		available.put("available", false);

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(userAvailability);
	}

	@PostMapping("/checkavailableemail")
	public ResponseEntity<?> checkAvailableEmail(@RequestParam(name = "email", required = true) String email) {
		boolean emailAvailable = this.userService.uniqueEmailAvailable(email);
		HashMap<String, Boolean> available = new HashMap<String, Boolean>();
		UserAvailability userAvailability = new UserAvailability();
		if (emailAvailable) {
			available.put("available", true);
			userAvailability.setAvailablity(available);
			return ResponseEntity.ok().body(userAvailability);
		}
		available.put("available", false);
		userAvailability.setAvailablity(available);
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(userAvailability);

	}

	@GetMapping("/{id}")
	public ResponseEntity<CurrentUserResponse> getRegisteredById(
			@ApiParam(value = "User id to retirieve the user information", required = true) @PathVariable("id") Long id)
			throws ResourceNotFoundException {
		User user = userService.findUserById(id).orElseThrow(
				() -> new ResourceNotFoundException("Resource not found which is associated with an id: " + id));

		CurrentUserResponse userResponse = new CurrentUserResponse(user.getId(), user.getName(), user.getEmail(), user.getImageUrl(), user.getRestaurant());
		return ResponseEntity.ok().body(userResponse);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
		userService.deleteUserById(id);
		return ResponseEntity.ok().body("Resource deleted successfully.");
	}

	@PostMapping("/logout")
	@ApiOperation(value = "User logout API. However it does not guarantee that the token will expire and become invalid.")
	public ResponseEntity<?> logout() {
		SecurityContextHolder.clearContext();
		return ResponseEntity.ok().body("Logget out successfully");
	}

}
