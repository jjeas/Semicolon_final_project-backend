package com.semicolon.backend.domain.attendance;

import com.semicolon.backend.domain.attendance.dto.AttendanceDTO;
import com.semicolon.backend.domain.attendance.service.AttendanceService;
import com.semicolon.backend.domain.registration.dto.RegistrationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/getMemberList/{lessonNo}")
    public ResponseEntity<List<AttendanceDTO>> getLessonMember(@AuthenticationPrincipal String loginIdFromToken, @PathVariable("lessonNo") Long lessonNo,
                                                               @RequestParam LocalDate date){
        log.info("제발요=>{}{}", lessonNo, date);
        return ResponseEntity.ok(attendanceService.getList(loginIdFromToken, lessonNo, date));
    }

    @PostMapping("/save/{lessonNo}")
    public ResponseEntity<String> saveAttendance(@AuthenticationPrincipal String loginIdFromToken, @RequestBody List<AttendanceDTO> requestList, @PathVariable("lessonNo") Long lessonNo){
        log.info("이거 들어오는지 확인=>{}{}", requestList, lessonNo);
        attendanceService.save(loginIdFromToken, requestList, lessonNo);
        return ResponseEntity.ok("출석 저장 완료");
    }
}
