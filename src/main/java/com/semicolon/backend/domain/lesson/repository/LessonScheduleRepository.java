package com.semicolon.backend.domain.lesson.repository;

import com.semicolon.backend.domain.lesson.entity.LessonSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonScheduleRepository extends JpaRepository<LessonSchedule, Long> {
}
