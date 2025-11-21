package com.semicolon.backend.domain.support.repository;

import com.semicolon.backend.domain.support.entity.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SupportRepository extends JpaRepository<Support, Long> {

    @Query("SELECT s FROM Support s LEFT JOIN FETCH s.response WHERE s.supportNo = :id")
    Optional<Support> findDetailWithResponse(Long id);
}
