package com.semicolon.backend.domain.schedule.repository;

import com.semicolon.backend.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("select s from Schedule s where s.startDate<=:end AND s.endDate>=:start ")
    List<Schedule> findDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
