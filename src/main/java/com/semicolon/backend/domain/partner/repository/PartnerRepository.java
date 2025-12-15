package com.semicolon.backend.domain.partner.repository;

import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.partner.entity.Partner;
import com.semicolon.backend.domain.partner.entity.PartnerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    @Query("SELECT p FROM Partner p WHERE p.member = :member ORDER BY p.requestDate DESC")
    Optional<Partner> findByMember(@Param("member") Member member);

    Page<Partner> findByStatus(PartnerStatus status, Pageable pageable);

    @Query("select p from Partner p where p.status = PartnerStatus.PENDING")
    List<Partner> findByStatusIsPending();
}
