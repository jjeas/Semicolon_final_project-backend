package com.semicolon.backend.domain.lesson;

import com.semicolon.backend.domain.lesson.dto.LessonReqDTO;
import com.semicolon.backend.domain.lesson.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @PostMapping("/lessonRequest")
    public ResponseEntity<String> lessonRequest(@AuthenticationPrincipal String loginIdFromToken, @RequestBody LessonReqDTO lessonReqDTO){
        lessonService.lessonReq(loginIdFromToken, lessonReqDTO);
        return ResponseEntity.ok("신청서 폼 받음");
    }

    @GetMapping("/myLessons")
    public ResponseEntity<List<LessonReqDTO>> getMyLessonList(@AuthenticationPrincipal String loginIdFromToken) {
        List<LessonReqDTO> dto = lessonService.getMyLessonList(loginIdFromToken);
        return ResponseEntity.ok(dto);
    }
}
