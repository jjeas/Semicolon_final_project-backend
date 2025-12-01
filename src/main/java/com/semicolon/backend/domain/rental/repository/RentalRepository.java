package com.semicolon.backend.domain.rental.repository;

import com.semicolon.backend.domain.rental.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("SELECT COUNT(r) FROM Rental r " +
            "WHERE r.space.id = :spaceId " +
            "AND r.startTime < :endTime AND r.endTime > :startTime")
    Long isReserved(@Param("spaceId") Long spaceId,
                          @Param("startTime") LocalDateTime startTime,
                          @Param("endTime") LocalDateTime endTime);
}
