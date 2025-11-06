package com.semicolon.backend.domain.faq.repository;

import com.semicolon.backend.domain.faq.entity.FaqCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqCategoryRepository extends JpaRepository<FaqCategory, Long> {
}
