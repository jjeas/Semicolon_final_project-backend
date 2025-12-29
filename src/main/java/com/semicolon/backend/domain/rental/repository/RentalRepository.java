package com.semicolon.backend.domain.rental.repository;

import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.rental.entity.Rental;
import com.semicolon.backend.domain.rental.entity.RentalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("SELECT COUNT(r) FROM Rental r " +
            "WHERE r.space.id = :spaceId " +
            "AND r.status <> 'REJECTED' " +
            "AND r.startTime < :endTime AND r.endTime > :startTime")
    Long isReserved(@Param("spaceId") Long spaceId,
                          @Param("startTime") LocalDateTime startTime,
                          @Param("endTime") LocalDateTime endTime);

    List<Rental> findByMember(Member member);

    @Query("""
    SELECT r FROM Rental r
    WHERE (:status IS NULL OR r.status = :status)
      AND (:keyword IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
      AND (:startDateTime IS NULL OR r.startTime >= :startDateTime)
      AND (:endDateTime IS NULL OR r.startTime <= :endDateTime)
    ORDER BY
      CASE r.status
          WHEN PENDING THEN 1
          WHEN ACCEPTED THEN 2
          WHEN REJECTED THEN 3
          ELSE 4
      END,
      r.startTime ASC
""")
    Page<Rental> searchAll(
            @Param("status") RentalStatus status,
            @Param("keyword") String keyword,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            Pageable pageable
    );
    @Query("select r from Rental r where r.status = RentalStatus.PENDING")
    List<Rental> findByStatusIsPending();

    @Query(value = "SELECT COUNT(*) FROM tbl_rental WHERE DATE(created_at) = CURDATE()", nativeQuery = true)
    long countRentalToday();

}
