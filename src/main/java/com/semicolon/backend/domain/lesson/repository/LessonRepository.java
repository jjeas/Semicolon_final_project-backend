package com.semicolon.backend.domain.lesson.repository;

import com.semicolon.backend.domain.lesson.dto.LessonListResDTO;
import com.semicolon.backend.domain.lesson.entity.Lesson;
import com.semicolon.backend.domain.lesson.entity.LessonDay;
import com.semicolon.backend.domain.lesson.entity.LessonStatus;
import com.semicolon.backend.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.semicolon.backend.domain.lesson.entity.LessonStatus.ACCEPTED;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByPartnerId(Member member);

    @Query("select distinct l from Lesson l " +
            "join l.facilitySpace fs join fs.facility f join l.partnerId p " +
            "join l.schedules s join s.lessonDay d " +
            "where (:category is null or :category = '' or f.facilityName = :category) and " +
            " (:titleKeyword is null or :titleKeyword = '' or l.title like concat('%', :titleKeyword, '%')) and " +
            " (:partnerKeyword is null or :partnerKeyword = '' or p.memberName like concat('%', :partnerKeyword, '%')) and " +
            " (d in (:days)) and " +
            " (not exists (select sc from l.schedules sc join sc.lessonDay le where le not in (:days))) and " +
            " (:startTime is null or :startTime = '' or function('DATE_FORMAT', s.startTime, '%H:%i') >= :startTime) and " +
            " (:endTime is null or :endTime = '' or function('DATE_FORMAT', s.endTime, '%H:%i') <= :endTime) and " +
            " ((:availableOnly is null or :availableOnly = false) or " +
            "  (l.lessonStatus = com.semicolon.backend.domain.lesson.entity.LessonStatus.ACCEPTED and " +
            "   not exists (select r from Registration r where r.lesson = l and r.member.memberLoginId = :loginId and r.status = RegistrationStatus.APPLIED)))"
    )
    Page<Lesson> searchLesson(
            @Param("category") String category,
            @Param("titleKeyword") String titleKeyword,
            @Param("partnerKeyword") String partnerKeyword,
            @Param("days") List<LessonDay> days,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("availableOnly") Boolean availableOnly,
            @Param("loginId") String loginId,
            Pageable pageable
    );

    @Query("select f.facilityName, count(r) from Registration r join r.lesson l join l.facilitySpace fs join fs.facility f group by f.facilityName")
    List<Object[]> findPopularityStats();

    @Query("select l from Lesson l join l.partnerId p where (:status is null or l.lessonStatus = :status) " +
            "and (:titleKeyword is null or LOWER(l.title) like LOWER(CONCAT('%', :titleKeyword, '%'))) " +
            "and (:partnerKeyword is null or LOWER(p.memberName) like LOWER(CONCAT('%', :partnerKeyword, '%'))) " +
            "order by case l.lessonStatus when 'PENDING' then 0 when 'ACCEPTED' then 1 when 'REJECTED' then 2 else 3 end, l.startDate asc")
    Page<Lesson> searchAdminLessonWithSort(
            @Param("status") LessonStatus status,
            @Param("titleKeyword") String titleKeyword,
            @Param("partnerKeyword") String partnerKeyword,
            Pageable pageable
    );
    @Query("""
  SELECT l FROM Lesson l 
  WHERE l.partnerId.memberLoginId = :loginId 
    AND l.title LIKE %:title%
""")
    List<Lesson> searchByTitle(@Param("loginId") String loginId,
                               @Param("title") String title);

    @Query("""
    SELECT l FROM Lesson l
    WHERE l.partnerId.memberLoginId = :loginId
    AND l.id = :lessonId
""")
    Lesson findLessonByPartnerAndId(@Param("loginId") String loginId,
                                    @Param("lessonId") Long lessonId);

    @Query("select l from Lesson l where l.lessonStatus = LessonStatus.PENDING")
    List<Lesson> findByStatusIsPending();
           
    List<Lesson> findTop7ByStartDateAfterAndLessonStatusOrderByStartDateAsc(
            LocalDate today,
            LessonStatus status
    );
    List<Lesson> findByLessonStatusAndStartDateBefore(LessonStatus status, LocalDate date);

}
