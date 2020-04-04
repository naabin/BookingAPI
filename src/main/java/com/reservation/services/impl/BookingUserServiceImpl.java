package com.reservation.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.reservation.models.security.BookingUser;
import com.reservation.models.security.UserRole;
import com.reservation.repositories.BookingUserRepository;
import com.reservation.repositories.RoleRepository;
import com.reservation.securityconfig.PasswordEncrypt;
import com.reservation.services.BookingUserService;

@Service
public class BookingUserServiceImpl implements BookingUserService{

	private static final Logger LOG = LoggerFactory.getLogger(BookingUserServiceImpl.class);

	private final BookingUserRepository userRepo;

	private final RoleRepository roleRepo;
	
	private  PasswordEncrypt passwordEncrypt;

	public BookingUserServiceImpl(BookingUserRepository userRepo, 
			RoleRepository roleRepo, PasswordEncrypt passwordEncrypt) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.passwordEncrypt = passwordEncrypt;
	}

	@Override
	@Transactional
	public BookingUser createUser(BookingUser user, Set<UserRole> userRoles) {
		BookingUser localUser = this.userRepo.findByEmail(user.getEmail());

		if (localUser != null) {
			LOG.info("User with username" + user.getUsername() + " already exists. ");
		} else {
			user.setPassword(this.passwordEncrypt.passwordEncoder().encode(user.getPassword()));
			user.setUsername(user.getUsername().toLowerCase());
			for (UserRole role : userRoles) {
				roleRepo.save(role.getRole());
			}

			user.getUserRoles().addAll(userRoles);

			localUser = userRepo.save(user);

		}
		return localUser;
	}

	@Override
	public BookingUser loadUserByEmail(String email) {

		BookingUser user = userRepo.findByEmail(email);
		if (user == null) {
			LOG.warn("User {} not found with email " + email);

			throw new UsernameNotFoundException(email + " not found");
		}

		return user;
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<BookingUser> getAllUsers() {
		return userRepo.findAll();
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	public Optional<BookingUser> findUserById(Long id) {
		return userRepo.findById(id);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteUserById(Long id) {
		userRepo.deleteById(id);
	}

	@Override
	public boolean uniqueUserAvailable(String username) {
		BookingUser findByUsername = this.userRepo.findUserByUsername(username);
		return !(findByUsername != null && findByUsername.getUsername().equals(username));
	}

	@Override
	public BookingUser updateUser(BookingUser user, Set<UserRole> userRoles) {
		for(UserRole userRole: userRoles) {
			this.roleRepo.saveAndFlush(userRole.getRole());
		}
		user.getUserRoles().addAll(userRoles);
		return this.userRepo.saveAndFlush(user);
	}

	@Override
	public BookingUser loadUserByUsername(String username) throws UsernameNotFoundException {
		BookingUser user = this.userRepo.findByEmail(username);
		if(user == null) {
			LOG.warn("User {} not found with username " + username);

			throw new UsernameNotFoundException(username + " not found");
		}
		return user;
	}

	@Override
	public boolean uniqueEmailAvailable(String email) {
		BookingUser findByEmail = this.userRepo.findByEmail(email);
		return !(findByEmail != null && findByEmail.getEmail().equals(email));
	}

	@Override
	public Optional<BookingUser> findUserByResetPin(Integer pin) {

		return this.userRepo.findUserByResetPin(pin);
	}


}
