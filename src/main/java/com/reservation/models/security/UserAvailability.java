package com.reservation.models.security;

import java.io.Serializable;
import java.util.HashMap;

public class UserAvailability implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HashMap<String, Boolean> availablity = new HashMap<String, Boolean>();
	
	
	public UserAvailability() {
		
	}
	
	public UserAvailability(HashMap<String, Boolean> availability) {
		this.availablity = availability;
	}

	public HashMap<String, Boolean> getAvailablity() {
		return availablity;
	}

	public void setAvailablity(HashMap<String, Boolean> availablity) {
		this.availablity = availablity;
	}
	
}
