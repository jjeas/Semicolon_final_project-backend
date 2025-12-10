package com.semicolon.backend.domain.attendance.repository;

import com.semicolon.backend.domain.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query("SELECT a FROM Attendance a " +
            "WHERE a.lesson.id = :lessonId " +
            "AND a.member.memberId = :memberId " +
            "AND a.attendanceDate = :attendanceDate")
    Optional<Attendance> findAttendance(
            @Param("lessonId") Long lessonId,
            @Param("memberId") Long memberId,
            @Param("attendanceDate") LocalDate attendanceDate
    );
}
