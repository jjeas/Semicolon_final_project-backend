package com.semicolon.backend.domain.lesson.repository;

import com.semicolon.backend.domain.lesson.entity.LessonDay;
import com.semicolon.backend.domain.lesson.entity.LessonSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public interface LessonScheduleRepository extends JpaRepository<LessonSchedule, Long> {

    @Query(value = "SELECT COUNT(*) FROM tbl_lesson_schedule ls " +
            "JOIN lesson_schedule_days lsd ON ls.lesson_Schedule_id = lsd.lesson_schedule_id " +
            "JOIN tbl_lesson l ON ls.lesson_id = l.lesson_id " +
            "WHERE l.facility_space_id = :spaceId " +
            "AND :targetDate BETWEEN l.lesson_start_date AND l.lesson_end_date " +
            "AND lsd.lesson_day = :lessonDay " +
            "AND ls.lesson_Schedule_start_Time < :endTime " +
            "AND ls.lesson_Schedule_end_Time > :startTime " +
            "AND l.lesson_status <> 'REJECTED'",
            nativeQuery = true)
    Long isLessonScheduled(@Param("spaceId") Long spaceId,
                           @Param("targetDate") LocalDate targetDate,
                           @Param("lessonDay") String lessonDay,
                           @Param("startTime") LocalTime startTime,
                           @Param("endTime") LocalTime endTime);
}
