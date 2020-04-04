package com.reservation.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.reservation.models.Reservation;

public interface ReservationService {
	
	Reservation createReservation(Reservation reservation);
	
	Reservation updateReservation(Reservation reservation);
	
	List<Reservation> getAllReservations();
	
	Page<Reservation> getReservations(Long restaurantId, Integer pageNumber, Integer pageSize, String property);
	
	Optional<Reservation> findById(Long id);
	
	void deleteReservationById(Long id);
	
	void deleteReservation(Reservation reservation);
	
	
}
