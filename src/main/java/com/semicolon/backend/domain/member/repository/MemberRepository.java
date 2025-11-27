package com.semicolon.backend.domain.member.repository;

import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.entity.MemberRole;
import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("""
    select new com.semicolon.backend.domain.partner.dto.PartnerDTO(p.status)
    from Partner p
    join p.member m
    where m.memberId = :memId
    order by p.requestNo desc
    fetch first 1 rows only
""")
    Optional<PartnerDTO> getPartnerStatus(@Param("memId") Long memId);
    Optional<Member> findByMemberLoginId(String memberLoginId);
    Optional<Member> findMemberIdByMemberNameAndMemberEmail(String memberName, String memberEmail);
    Optional<Member> findMemberByMemberNameAndMemberEmailAndMemberLoginId(String memberName, String memberEmail, String memberLoginId);
    boolean existsByMemberEmail(String memberEmail);
    boolean existsByMemberLoginId(String memberLoginId);
    List<Member> findByMemberLoginIdContains(String keyword);
    List<Member> findByMemberNameContains(String keyword);
    List<Member> findByMemberRole(MemberRole role);
    List<Member> findByMemberLoginIdContainsAndMemberRole(String keyword, MemberRole role);
    List<Member> findByMemberNameContainsAndMemberRole(String keyword, MemberRole role);
}