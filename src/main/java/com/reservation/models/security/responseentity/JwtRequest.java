package com.reservation.models.security.responseentity;

import java.io.Serializable;

public class JwtRequest implements Serializable {

	private static final long serialVersionUID = -3438110967838405933L;

	private String username;
	private String password;

	public JwtRequest() {

	}

	public JwtRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
