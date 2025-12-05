package com.semicolon.backend.domain.program;

import com.semicolon.backend.domain.program.dto.ProgramDTO;
import com.semicolon.backend.domain.program.dto.ProgramReqDTO;
import com.semicolon.backend.domain.program.entity.Program;
import com.semicolon.backend.domain.program.service.ProgramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/program")
public class ProgramController {

    @Autowired
    private ProgramService service;

    @GetMapping("/{programId}")
    public ResponseEntity<ProgramDTO> getOne(@PathVariable("programId") long programId ){
        log.info("조회 ID확인 = > {}",programId);
        ProgramDTO dto = service.getOne(programId);
        log.info("dto => {}",dto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{programId}")
    public ResponseEntity<String> update(@PathVariable("programId") long programId, @ModelAttribute ProgramReqDTO programDTO ){
        log.info("수정 ID확인 = > {}",programId);
        ProgramDTO dto = service.getOne(programId);
        if(dto.getPno()==programDTO.getPno()) service.update(programDTO);
        return ResponseEntity.ok("수정 성공");
    }

    @GetMapping("/")
    public ResponseEntity<List<ProgramDTO>> getList(){
        return ResponseEntity.ok(service.getList());
    }

}