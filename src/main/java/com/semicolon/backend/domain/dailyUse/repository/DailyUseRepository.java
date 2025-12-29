package com.semicolon.backend.domain.dailyUse.repository;

import com.semicolon.backend.domain.dailyUse.entity.DailyUse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyUseRepository extends JpaRepository<DailyUse, Long> {

    @Query("SELECT COUNT(d) FROM DailyUse d " +
            "WHERE d.space.id = :spaceId " +
            "AND d.startTime < :endTime AND d.endTime > :startTime")
    Long isReserved(@Param("spaceId") Long spaceId,
                          @Param("startTime") LocalDateTime startTime,
                          @Param("endTime") LocalDateTime endTime);

    List<DailyUse> findByMemberMemberId(Long memberId);

    @Query(value = "SELECT COUNT(*) FROM tbl_daily_use WHERE DATE(created_at) = CURDATE()", nativeQuery = true)
    long countDailyUseToday();

}
