package com.semicolon.backend.domain.program.service;

import com.semicolon.backend.domain.program.dto.ProgramDTO;
import com.semicolon.backend.domain.program.entity.Program;
import com.semicolon.backend.domain.program.repository.ProgramRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramServiceImpl implements ProgramService{

    @Autowired
    private ProgramRepository repository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public ProgramDTO getOne(long pno) {
        ProgramDTO dto = mapper.map(repository.findById(pno), ProgramDTO.class);
        return dto;
    }

    @Override
    public Long register(ProgramDTO program) {
        Program dto = repository.save(mapper.map(program, Program.class));
        return dto.getPno();
    }

    @Override
    public List<ProgramDTO> getList() {
        List<ProgramDTO> list = repository.findAll().stream().map(i->mapper.map(i,ProgramDTO.class)).toList();
        return list;
    }
}