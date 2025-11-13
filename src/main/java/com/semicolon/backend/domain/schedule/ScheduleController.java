package com.semicolon.backend.domain.schedule;

import com.semicolon.backend.domain.schedule.dto.ScheduleDTO;
import com.semicolon.backend.domain.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService service;
    @GetMapping("")
    public ResponseEntity<List<ScheduleDTO>> getSchedules(@RequestParam(name = "startDate") LocalDate start, @RequestParam(name = "endDate") LocalDate end ){
        return ResponseEntity.ok(service.findScheduleByDate(start, end));
    }
}
