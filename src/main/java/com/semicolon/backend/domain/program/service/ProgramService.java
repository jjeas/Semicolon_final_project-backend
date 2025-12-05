package com.semicolon.backend.domain.program.service;

import com.semicolon.backend.domain.program.dto.ProgramDTO;
import com.semicolon.backend.domain.program.dto.ProgramReqDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ProgramService {
    public ProgramDTO getOne(long pno);
    public void update(ProgramReqDTO programReqDTO);
    public List<ProgramDTO> getList();

}