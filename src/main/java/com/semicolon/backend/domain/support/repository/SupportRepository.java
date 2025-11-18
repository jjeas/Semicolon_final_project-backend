package com.semicolon.backend.domain.support.repository;

import com.semicolon.backend.domain.support.entity.Support;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportRepository extends JpaRepository<Support, Long> {
}
