package com.reservation.models;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "reservation")
@ApiModel(description = "Details about reservation")
public class Reservation  extends Auditable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(notes = "Auto genereated ID from the database")
	private Long id;

	@ApiModelProperty(notes = "Full name of person who is making reservation")
	@Column(name = "full_name")
	@NotNull
	private String fullName;

	@ApiModelProperty(notes = "Email of the person who is making reservation")
	@Column(name = "email")
	@NotNull
	private String email;

	@ApiModelProperty(notes = "Phone number of the person who is making reservation")
	@Column(name = "phone_number")
	@NotNull
	private String phoneNumber;

	@ApiModelProperty(notes = "Number of people for the reservation")
	@Column(name = "numner_of_peoeple")
	@NotNull
	private Integer numberOfPeople;

	@ApiModelProperty(notes = "Date of reservation")
	@Column(name = "date")
	@NotNull
	private LocalDate date;
	
	@ApiModelProperty(notes = "Time of reservation")
	@Column(name = "time")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXX")
	@NotNull
	private OffsetDateTime time;

	@ApiModelProperty(notes = "Special request of made for reservation")
	@Column(name = "special_request", columnDefinition = "text")
	private String specialRequest;
	
	@Column
	private Boolean confimed;
	
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public OffsetDateTime getTime() {
		return time;
	}

	public void setTime(OffsetDateTime time) {
		this.time = time;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public Integer getNumberOfPeople() {
		return numberOfPeople;
	}

	public void setNumberOfPeople(Integer numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		if (date.isBefore(LocalDate.now())) {
			this.date = LocalDate.now();
		}
		this.date = date;
	}

	public String getSpecialRequest() {
		return specialRequest;
	}

	public void setSpecialRequest(String specialRequest) {
		this.specialRequest = specialRequest;
	}
	

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	

	public Boolean getConfimed() {
		return confimed;
	}

	public void setConfimed(Boolean confimed) {
		this.confimed = confimed;
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", fullName=" + fullName + ", email=" + email + ", phoneNumber=" + phoneNumber
				+ ", numberOfPeople=" + numberOfPeople + ", date=" + date + ", specialRequest=" + specialRequest + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((numberOfPeople == null) ? 0 : numberOfPeople.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((specialRequest == null) ? 0 : specialRequest.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reservation other = (Reservation) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (numberOfPeople == null) {
			if (other.numberOfPeople != null)
				return false;
		} else if (!numberOfPeople.equals(other.numberOfPeople))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (specialRequest == null) {
			if (other.specialRequest != null)
				return false;
		} else if (!specialRequest.equals(other.specialRequest))
			return false;
		return true;
	}

}
