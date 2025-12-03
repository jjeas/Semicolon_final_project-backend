package com.semicolon.backend.domain.dailyUse.repository;

import com.semicolon.backend.domain.dailyUse.entity.GymDailyUse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface GymDailyUseRepository extends JpaRepository<GymDailyUse, Long> {

    @Modifying
    @Query("UPDATE GymDailyUse g SET g.status = 'DONE' WHERE g.date < :today")
    int updateExpired(@Param("today") LocalDate today);

}
