package com.semicolon.backend.domain.schedule;

import com.semicolon.backend.domain.faq.dto.FaqDTO;
import com.semicolon.backend.domain.schedule.dto.ScheduleDTO;
import com.semicolon.backend.domain.schedule.service.ScheduleService;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/community/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService service;

    @GetMapping("")
    public ResponseEntity<List<ScheduleDTO>> getSchedules(@RequestParam(name = "startDate") LocalDate start, @RequestParam(name = "endDate") LocalDate end ){
        return ResponseEntity.ok(service.findScheduleByDate(start, end));
    }

    @GetMapping("/admin")
    public ResponseEntity<PageResponseDTO<ScheduleDTO>> getAllList(PageRequestDTO pageRequestDTO){
        return ResponseEntity.ok(service.getList(pageRequestDTO));
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<String> updateSchedule(@RequestBody ScheduleDTO dto, @PathVariable("id") long id){
        service.update(dto,id);
        return ResponseEntity.ok("Schedule 업데이트 성공");
    }

    @PostMapping("/admin/register")
    public ResponseEntity<String> registerSchedule(@RequestBody ScheduleDTO dto){
        service.register(dto);
        return ResponseEntity.ok("Schedule 등록 성공");
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable("id") Long id){
        service.delete(id);
        return ResponseEntity.ok("Schedule 삭제 완료");
    }

    @GetMapping("/admin/monthly")
    public ResponseEntity<List<ScheduleDTO>> getMonthlySchedules() {
        return ResponseEntity.ok(service.getSchedulesCurrentMonth());
    }
}
