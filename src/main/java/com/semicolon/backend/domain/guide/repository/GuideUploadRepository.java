package com.semicolon.backend.domain.guide.repository;

import com.semicolon.backend.domain.guide.entity.GuideUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuideUploadRepository extends JpaRepository<GuideUpload, Long> {
}
