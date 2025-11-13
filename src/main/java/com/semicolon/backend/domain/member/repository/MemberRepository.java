package com.semicolon.backend.domain.member.repository;

import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("""
    select new com.semicolon.backend.domain.partner.dto.PartnerDTO(p.status)
    from Partner p
    join p.member m
    where m.memberId = :memId
""")
    Optional<PartnerDTO> getPartnerStatus(@Param("memId") Long memId);

}