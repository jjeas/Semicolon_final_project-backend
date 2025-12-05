package com.semicolon.backend.domain.lesson.repository;

import com.semicolon.backend.domain.lesson.entity.Lesson;
import com.semicolon.backend.domain.lesson.entity.LessonDay;
import com.semicolon.backend.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByPartnerId(Member member);

    @Query("select distinct l from Lesson l join l.facilitySpace fs join fs.facility f join l.partnerId p join l.schedules s join s.lessonDay d "+
            "where (:category is null or f.facilityName = :category ) and " +
            " (:titleKeyword is null or l.title like %:titleKeyword% ) and " +
            " (:partnerKeyword is null or p.memberName like %:partnerKeyword% ) and " +
            " (d in (:days)) and " +
            " (:startTime is null or TO_CHAR(s.startTime, 'HH24:MI') >= :startTime) and " +
            " (:endTime is null or TO_CHAR(s.endTime, 'HH24:MI') <= :endTime) and " +
            " (:availableOnly is null or :availableOnly =false or l.lessonStatus=com.semicolon.backend.domain.lesson.entity.LessonStatus.ACCEPTED)"
    )
    Page<Lesson> searchLesson(
            @Param("category") String category,
            @Param("titleKeyword") String titleKeyword,
            @Param("partnerKeyword") String partnerKeyword,
            @Param("days") List<LessonDay> days,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("availableOnly") Boolean availableOnly,
            Pageable pageable
    );

    @Query("select f.facilityName, count(r) from Registration r join r.lesson l join l.facilitySpace fs join fs.facility f group by f.facilityName")
    List<Object[]> findPopularityStats();

}
