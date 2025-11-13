package com.semicolon.backend.domain.partner.repository;

import com.semicolon.backend.domain.partner.entity.PartnerFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerFileRepository extends JpaRepository<PartnerFile, Long> {
}
