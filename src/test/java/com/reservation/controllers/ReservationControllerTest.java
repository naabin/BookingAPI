package com.reservation.controllers;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.reservation.models.Reservation;
import com.reservation.services.ReservationService;


@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {
	
	@Mock
	ReservationService reservationService;
	
 	@InjectMocks
 	ReservationController reservationContoller;
 	
 	List<Reservation> reservations;
 	
 	MockMvc mockMvc;
 	
 	final static Long ID=1L;
 	

	@BeforeEach
	void setUp() throws Exception {
		reservations =  new ArrayList<Reservation>();
		for(long i = 0; i < 3; i++) {
			Reservation reservation = new Reservation();
			reservation.setId(i);
			reservations.add(reservation);
		}
		
		mockMvc = MockMvcBuilders.standaloneSetup(reservationContoller).build();
	}
//	@Test
//	void testGetAllReservation() throws Exception {
//		when(reservationService.getAllReservations()).thenReturn(reservations);
//		
//		mockMvc.perform(get("/api/reservation/all"))
//			.andExpect(status().isOk());
//		
//		
//		assertTrue(reservations.size() > 0);
//		assertEquals(3, reservations.size());
//			
//	}
//
//	@Test
//	void testGetReservationById() throws Exception {
//		Reservation reservation = new Reservation();
//		reservation.setId(ID);
//		
//		when(reservationService.findById(Mockito.anyLong())).thenReturn(Optional.of(reservation));
//		
//		mockMvc.perform(get("/api/reservation/1"))
//			.andExpect(status().isOk());
//		
//		
//		assertNotNull(reservation);
//		assertEquals(ID, reservation.getId());
//		
//	}

	@Test
	void testCreateReservation() throws Exception {
		
//		Reservation r1 = new Reservation();
//		r1.setId(ID);
//		r1.setFullName("Test Tester");
//		
//		when(reservationService.createReservation(Mockito.any())).thenReturn(r1);
//		
//		mockMvc.perform(post("/api/reservation/new"))
//			.andExpect(status().isOk());
		
		
		
	}

	@Test
	void testUpdateReservation() {
		
	}

	@Test
	void testDeleteReservationById() {
		
	}

	@Test
	void testDeleteReservation() {
		
	}

}
