package com.semicolon.backend.domain.program.service;

import com.semicolon.backend.domain.program.dto.ProgramDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ProgramService {
    public ProgramDTO getOne(long pno);
    public Long register(ProgramDTO program);
    public List<ProgramDTO> getList();

}