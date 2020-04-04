package com.reservation.services;

import java.util.Optional;

public interface GeneralService<T> {

	T save(T t);
	Optional<T> findById(long id);
	T update(T t);
	void delete(long id);
}
