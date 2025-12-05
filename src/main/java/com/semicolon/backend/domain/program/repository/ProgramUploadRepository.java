package com.semicolon.backend.domain.program.repository;

import com.semicolon.backend.domain.program.entity.ProgramUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramUploadRepository extends JpaRepository<ProgramUpload,Long> {
}
