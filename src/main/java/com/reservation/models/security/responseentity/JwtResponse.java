package com.reservation.models.security.responseentity;

import java.io.Serializable;


public class JwtResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private final String jwtToken;
	private final Long id;
	private final String name;
	private final String email;
	private Long restaurantId;


	public JwtResponse(String jwtToken, Long id,String name, String email, Long restaurantId) {
		this.jwtToken = jwtToken;
		this.id = id;
		this.email = email;
		this.name = name;
		this.restaurantId =restaurantId;
	}
	
	public JwtResponse(String jwtToken, Long id,String name, String email) {
		this.jwtToken = jwtToken;
		this.id = id;
		this.name = name;
		this.email = email;
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
	
	

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}
	
	public Long getRestaurantId() {
		return restaurantId;
	}
	
	
	

}
