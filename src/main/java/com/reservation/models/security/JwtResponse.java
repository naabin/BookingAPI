package com.reservation.models.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class JwtResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private final String jwtToken;
	private final Long id;
	private final String firstName;
	private final String lastName;
	private final String email;
	private Long restaurantId;
	private Set<UserRole> userRoles = new HashSet<UserRole>();


	public JwtResponse(String jwtToken, Long id, String firstName, String lastName, String email,
			Set<UserRole> userRoles,Long restaurantId) {
		this.jwtToken = jwtToken;
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userRoles = userRoles;
		this.restaurantId =restaurantId;
	}
	
	public JwtResponse(String jwtToken, Long id, String firstName, String lastName, String email,
			Set<UserRole> userRoles) {
		this.jwtToken = jwtToken;
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userRoles = userRoles;
	}
	
	public String getJwtToken() {
		return jwtToken;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}
	
	public Long getRestaurantId() {
		return restaurantId;
	}
	
	
	

}
