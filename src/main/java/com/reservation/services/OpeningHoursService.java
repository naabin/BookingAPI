package com.reservation.services;

import java.util.List;
import java.util.Optional;

import com.reservation.models.OpeningHours;

public interface OpeningHoursService {
	
	OpeningHours createOpeningHours(OpeningHours openingHours);
	
	OpeningHours updateOpeningHours(OpeningHours openingHours);
	
	List<OpeningHours> getAllOpeningHours();
	
	Optional<OpeningHours> findById(Long id);
	
	void deleteOpeningHoursById(Long id);
	
	void deleteOpeningHours(OpeningHours openingHours);
}
