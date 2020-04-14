package com.reservation.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.reservation.models.security.User;

public interface UserService extends UserDetailsService {
	
	
	User createUser(User user);
	
	List<User> getAllUsers();
	
	Optional<User> findUserById(Long id);
	
	void deleteUserById(Long id);
	
	User loadUserByEmail(String email);
	
	User updateUser(User user);
	
	boolean uniqueUserAvailable(String username);
	
	boolean uniqueEmailAvailable(String email);
	
	Optional<User> findUserByResetPin(Integer pin);
}
