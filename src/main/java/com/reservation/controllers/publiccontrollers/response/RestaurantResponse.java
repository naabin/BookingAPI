package com.reservation.controllers.publiccontrollers.response;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.reservation.models.OpeningHours;

public class RestaurantResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String email;
	private String phoneNumber;
	private String siteAddress;
	private String about;
	private Set<OpeningHours> openingHours = new HashSet<OpeningHours>();
	
	
	public RestaurantResponse(Long id, String name, String email, String phoneNumber, String siteAddress,
			String about,
			Set<OpeningHours> openingHours) {
		
		this.id = id;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.siteAddress = siteAddress;
		this.about = about;
		this.openingHours = openingHours;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getSiteAddress() {
		return siteAddress;
	}


	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}


	public Set<OpeningHours> getOpeningHours() {
		return openingHours;
	}


	public void setOpeningHours(Set<OpeningHours> openingHours) {
		this.openingHours = openingHours;
	}


	public String getAbout() {
		return about;
	}


	public void setAbout(String about) {
		this.about = about;
	}
	
	
	
	
	
	

}
