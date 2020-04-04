package com.reservation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reservation.models.OpeningHours;

@Repository
public interface OpeningHoursRepository extends JpaRepository<OpeningHours, Long> {
	
}
