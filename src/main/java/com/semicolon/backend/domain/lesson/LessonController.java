package com.semicolon.backend.domain.lesson;

import com.semicolon.backend.domain.lesson.dto.LessonReqDTO;
import com.semicolon.backend.domain.lesson.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @PostMapping("/lessonRequest")
    public ResponseEntity<String> lessonRequest(@AuthenticationPrincipal String loginIdFromToken, @RequestBody LessonReqDTO lessonReqDTO){
        log.info("lessondto=>{}", lessonReqDTO);
        lessonService.lessonReq(loginIdFromToken, lessonReqDTO);
        return ResponseEntity.ok("신청서 폼 받음");
    }
}
