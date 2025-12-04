package com.semicolon.backend.domain.attendance.repository;

import com.semicolon.backend.domain.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
