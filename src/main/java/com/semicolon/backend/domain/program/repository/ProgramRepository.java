package com.semicolon.backend.domain.program.repository;

import com.semicolon.backend.domain.program.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program,Long> {
}
