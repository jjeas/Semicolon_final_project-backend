package com.semicolon.backend.domain.support.repository;

import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.support.entity.Support;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SupportRepository extends JpaRepository<Support, Long> {

    @Query("SELECT s FROM Support s LEFT JOIN FETCH s.response WHERE s.supportNo = :id")
    Optional<Support> findDetailWithResponse(Long id);

    @Query("SELECT s FROM Support s ORDER BY CASE WHEN s.status = 'WAITING' THEN 0 ELSE 1 END, s.createdDate DESC")
    Page<Support> findAllOrderByStatusAndDate(Pageable pageable);
    Page<Support> findByMemberMemberNameContains(String keyword, Pageable pageable);
    Page<Support> findBySupportTitleContains(String keyword, Pageable pageable);

    @Query("SELECT COUNT(*) from Support s where s.status = SupportStatus.WAITING")
    long countStatusIsWaiting();

}
