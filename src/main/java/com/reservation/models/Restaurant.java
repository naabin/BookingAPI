package com.reservation.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.reservation.models.security.BookingUser;

import io.swagger.annotations.ApiModel;

@Entity
@Table(name = "restaurant")
@ApiModel(description = "Details of Restaurant")
public class Restaurant extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String name;

	@NotNull
	private String address;

	@NotNull
	private String phoneNumber;
	
	@Column
	private String siteAddress;
	
	@Column
	private String email;
	
	@Column(columnDefinition = "text")
	private String about;
	
	@Column
	private String generatedUrl;
	

	@OneToMany(cascade = CascadeType.ALL,mappedBy = "restaurantOpeningHours")
	private List<OpeningHours> openingHours = new ArrayList<OpeningHours>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "restaurant")
	private List<Reservation> bookings = new ArrayList<Reservation>();

	
	@OneToOne
	private Image image;

	@OneToOne
	private BookingUser user;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public List<OpeningHours> getOpeningHours() {
		return openingHours;
	}

	public void setOpeningHours(List<OpeningHours> openingHours) {
		this.openingHours = openingHours;
	}

	public List<Reservation> getBookings() {
		return bookings;
	}

	public void setBookings(List<Reservation> bookings) {
		this.bookings = bookings;
	}

	public BookingUser getUser() {
		return user;
	}

	public void setUser(BookingUser user) {
		this.user = user;
	}

	public String getGeneratedUrl() {
		return generatedUrl;
	}

	public void setGeneratedUrl(String generatedUrl) {
		this.generatedUrl = generatedUrl;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	
	
	

}
