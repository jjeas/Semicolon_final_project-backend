package com.semicolon.backend.domain.lesson.repository;

import com.semicolon.backend.domain.lesson.entity.Lesson;
import com.semicolon.backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByPartnerId(Member member);
}
