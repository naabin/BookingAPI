package com.reservation.services.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.reservation.models.security.User;
import com.reservation.models.security.oauthuser.UserPrincipal;
import com.reservation.repositories.UserRepository;
import com.reservation.securityconfig.PasswordEncrypt;
import com.reservation.services.UserService;

@Service
public class UserServiceImpl implements UserService{

	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository userRepo;
	
	private  PasswordEncrypt passwordEncrypt;

	public UserServiceImpl(UserRepository userRepo, PasswordEncrypt passwordEncrypt) {
		this.userRepo = userRepo;
		this.passwordEncrypt = passwordEncrypt;
	}

	@Override
	@Transactional
	public User createUser(User user) {
		User localUser = this.userRepo.findByEmail(user.getEmail());

		if (localUser != null) {
			LOG.info("User with username" + user.getName() + " already exists. ");
		} else {
			user.setPassword(this.passwordEncrypt.passwordEncoder().encode(user.getPassword()));
			localUser = userRepo.save(user);
		}
		return localUser;
	}

	@Override
	public User loadUserByEmail(String email) {

		User user = userRepo.findByEmail(email);
		if (user == null) {
			LOG.warn("User {} not found with email " + email);

			throw new UsernameNotFoundException(email + " not found");
		}

		return user;
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	public Optional<User> findUserById(Long id) {
		return userRepo.findById(id);
	}

	@Override
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteUserById(Long id) {
		userRepo.deleteById(id);
	}

	@Override
	public boolean uniqueUserAvailable(String username) {
		User findByUsername = this.userRepo.findUserByName(username);
		return !(findByUsername != null && findByUsername.getName().equals(username));
	}

	@Override
	public User updateUser(User user) {
		return this.userRepo.saveAndFlush(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRepo.findByEmail(username);
		if(user == null) {
			LOG.warn("User {} not found with username " + username);

			throw new UsernameNotFoundException(username + " not found");
		}
		return UserPrincipal.create(user);
	}

	@Override
	public boolean uniqueEmailAvailable(String email) {
		User findByEmail = this.userRepo.findByEmail(email);
		return !(findByEmail != null && findByEmail.getEmail().equals(email));
	}

	@Override
	public Optional<User> findUserByResetPin(Integer pin) {

		 return this.userRepo.findUserByResetPin(pin);
	}


}
