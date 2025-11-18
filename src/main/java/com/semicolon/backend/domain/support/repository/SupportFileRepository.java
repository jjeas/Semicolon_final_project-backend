package com.semicolon.backend.domain.support.repository;

import com.semicolon.backend.domain.support.entity.SupportFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportFileRepository extends JpaRepository<SupportFile, Long> {
}
