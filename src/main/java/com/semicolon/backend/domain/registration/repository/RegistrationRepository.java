package com.semicolon.backend.domain.registration.repository;

import com.semicolon.backend.domain.lesson.entity.Lesson;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.registration.entity.Registration;
import com.semicolon.backend.domain.registration.entity.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration,Long> {
    boolean existsByMemberAndLessonAndStatus(Member member, Lesson lesson, RegistrationStatus status);

    @Query("select r from Registration r join fetch r.lesson where r.member.memberId=:memberId")
    List<Registration> findByMemberId(@Param("memberId") Long memberId);


    @Query("select r.lesson.id from Registration r where r.member.memberLoginId=:loginId and r.status=com.semicolon.backend.domain.registration.entity.RegistrationStatus.APPLIED")
    List<Long> findRegisteredLessonId(@Param("loginId") String loginId);

    long countByLesson_IdAndStatus(Long lessonId, RegistrationStatus status);

    @Query("select r from Registration r WHERE r.lesson.id = :lessonId")
    List<Registration> findAllByLessonId(@Param("lessonId") Long lessonId);
}
