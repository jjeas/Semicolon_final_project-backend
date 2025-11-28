package com.semicolon.backend.domain.lesson.repository;

import com.semicolon.backend.domain.lesson.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
