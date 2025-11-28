package com.semicolon.backend.domain.facility.repository;

import com.semicolon.backend.domain.facility.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
}
