package com.semicolon.backend.domain.member.repository;

import com.semicolon.backend.domain.member.dto.MemberGenderAgeDTO;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.entity.MemberRole;
import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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
    Page<Member> findByMemberLoginIdContains(String keyword, Pageable pageable);
    Page<Member> findByMemberNameContains(String keyword, Pageable pageable);
    Page<Member> findByMemberRole(MemberRole role, Pageable pageable);
    Page<Member> findByMemberLoginIdContainsAndMemberRole(String keyword, MemberRole role, Pageable pageable);
    Page<Member> findByMemberNameContainsAndMemberRole(String keyword, MemberRole role, Pageable pageable);

    @Query(value =
            "SELECT T.ageGroup AS ageGroup, T.MEMBER_GENDER AS gender, COUNT(1) AS count " +
                    "FROM ( " +
                    "    SELECT m.MEMBER_GENDER, " + // [수정] 실제 컬럼명 MEMBER_GENDER 사용
                    "      CASE " +
                    "        WHEN (:currentYear - EXTRACT(YEAR FROM m.BIRTH_DATE)) < 20 THEN '10대 이하' " +
                    "        WHEN (:currentYear - EXTRACT(YEAR FROM m.BIRTH_DATE)) BETWEEN 20 AND 29 THEN '20대' " +
                    "        WHEN (:currentYear - EXTRACT(YEAR FROM m.BIRTH_DATE)) BETWEEN 30 AND 39 THEN '30대' " +
                    "        WHEN (:currentYear - EXTRACT(YEAR FROM m.BIRTH_DATE)) BETWEEN 40 AND 49 THEN '40대' " +
                    "        WHEN (:currentYear - EXTRACT(YEAR FROM m.BIRTH_DATE)) BETWEEN 50 AND 59 THEN '50대' " +
                    "        ELSE '60대 이상' " +
                    "      END AS ageGroup " +
                    "    FROM tbl_member m " +
                    "    WHERE m.member_role='ROLE_USER' " +
                    ") T " +
                    "GROUP BY T.ageGroup, T.MEMBER_GENDER " + // [수정] 그룹핑도 MEMBER_GENDER로
                    "ORDER BY T.ageGroup, T.MEMBER_GENDER",
            nativeQuery = true)
    List<MemberGenderAgeDTO> getAgeGenderGroupStats(@Param("currentYear") int currentYear);
    long countByMemberRole(MemberRole memberRole);
}