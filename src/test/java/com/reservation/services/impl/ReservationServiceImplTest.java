package com.reservation.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.reservation.models.Reservation;
import com.reservation.repositories.ReservationRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

	@Mock
	private ReservationRepository reservationRepository;

	@InjectMocks
	private ReservationServiceImpl reservationService;
	
	@InjectMocks
	private RestaurantServiceImpl restaurantService;

	private Reservation reservation;

	private static final Long ID = 1L;

	@BeforeEach
	void setUp() throws Exception {
		reservation = new Reservation();
		reservation.setId(ID);
		reservation.setFullName("John Doe");
		reservation.setEmail("john@email.com");
		reservation.setPhoneNumber("0123456789");
		reservation.setNumberOfPeople(2);
		reservation.setDate(LocalDate.now());
	}

	@Test
	void testCreateReservation() {
		Reservation r1 = new Reservation();
		r1.setId(2L);
		r1.setFullName("John");
		r1.setEmail("john@email.com");
		r1.setPhoneNumber("01234556789");
		r1.setNumberOfPeople(2);
		r1.setDate(LocalDate.now());
		r1.setSpecialRequest("This is test");
		
		when(this.reservationRepository.save(Mockito.any())).thenReturn(reservation);

		Reservation savedReservation = this.reservationService.createReservation(r1);

		assertNotNull(savedReservation);

		assertEquals(ID, savedReservation.getId());
	}

//	@Test
//	void testUpdateReservation() {
//
//		reservation.setId(2L);
//		reservation.setFullName("Tony");
//		when(this.reservationRepository.save(Mockito.any())).thenReturn(reservation);
//		Reservation updatedReservation = this.reservationService.updateReservation(reservation);
//		assertEquals(2L, updatedReservation.getId());
//		assertEquals("Tony", updatedReservation.getFullName());
//
//	}

	@Test
	void testGetAllReservations() {
		List<Reservation> reservations = new ArrayList<Reservation>();
		for (long i = 0; i < 3; i++) {
			Reservation reservation = new Reservation();
			reservation.setId(i);
			reservation.setFullName("Test " + i);
			reservations.add(reservation);
		}

		when(this.reservationRepository.findAll()).thenReturn(reservations);
		List<Reservation> allReservations = this.reservationService.getAllReservations();

		assertNotNull(allReservations);
		assertEquals(3, allReservations.size());
	}

	@Test
	void testFindById() {
		when(this.reservationRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(this.reservation));
		Optional<Reservation> reservationById = this.reservationService.findById(this.reservation.getId());

		assertNotNull(reservationById.get());
		assertEquals(ID, reservationById.get().getId());
	}

	@Test
	void testNotFoundById() {
		when(this.reservationRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		Optional<Reservation> findById = this.reservationService.findById(ID);
		if(findById.isPresent()) {
			assertNull(findById.get());
		}
		

	}

	@Test
	void testDeleteReservationById() {
		this.reservationService.deleteReservationById(ID);
		verify(this.reservationRepository).deleteById(Mockito.anyLong());
	}

	@Test
	void testDeleteReservation() {
		this.reservationService.deleteReservation(reservation);
		verify(this.reservationRepository).delete(Mockito.any());
	}

}
