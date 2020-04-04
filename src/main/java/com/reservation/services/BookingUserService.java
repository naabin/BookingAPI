package com.reservation.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.reservation.models.security.BookingUser;
import com.reservation.models.security.UserRole;

public interface BookingUserService extends UserDetailsService {
	
	
	BookingUser createUser(BookingUser user, Set<UserRole> userRoles);
	
	List<BookingUser> getAllUsers();
	
	Optional<BookingUser> findUserById(Long id);
	
	void deleteUserById(Long id);
	
	BookingUser loadUserByEmail(String email);
	
	BookingUser updateUser(BookingUser user, Set<UserRole> userRoles);
	
	boolean uniqueUserAvailable(String username);
	
	boolean uniqueEmailAvailable(String email);
	
	Optional<BookingUser> findUserByResetPin(Integer pin);
}
