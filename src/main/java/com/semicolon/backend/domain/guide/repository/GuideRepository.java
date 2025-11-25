package com.semicolon.backend.domain.guide.repository;

import com.semicolon.backend.domain.guide.entity.Guide;
import com.semicolon.backend.domain.guide.entity.GuideCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuideRepository extends JpaRepository<Guide, Long> {
    Optional<Guide> findByCategory (GuideCategory category);
}
