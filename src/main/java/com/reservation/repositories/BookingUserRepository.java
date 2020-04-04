package com.reservation.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reservation.models.security.BookingUser;

@Repository
public interface BookingUserRepository extends JpaRepository<BookingUser, Long> {

	BookingUser findByEmail(String email);
	
	BookingUser findUserByUsername(String username);
	
	Optional<BookingUser> findUserByResetPin(Integer resetPin);
}
