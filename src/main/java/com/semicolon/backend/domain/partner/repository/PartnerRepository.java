package com.semicolon.backend.domain.partner.repository;

import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.partner.entity.Partner;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartnerRepository extends JpaRepository<Partner, Long> {

}
