package com.reservation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reservation.models.security.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
