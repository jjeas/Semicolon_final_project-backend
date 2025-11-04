package com.semicolon.backend.domain.program;

import com.semicolon.backend.domain.program.dto.ProgramDTO;
import com.semicolon.backend.domain.program.service.ProgramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/program")
public class ProgramController {

    @Autowired
    private ProgramService service;

    @GetMapping("/{programId}")
    public ResponseEntity<ProgramDTO> getOne(@PathVariable("programId") long programId ){
        log.info("ID확인 = > {}",programId);
        ProgramDTO dto = service.getOne(programId);
        log.info("dto => {}",dto);
        return ResponseEntity.ok(dto);
    }

}