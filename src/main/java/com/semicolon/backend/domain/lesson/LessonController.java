package com.semicolon.backend.domain.lesson;

import com.semicolon.backend.domain.lesson.dto.LessonListResDTO;
import com.semicolon.backend.domain.lesson.dto.LessonReqDTO;
import com.semicolon.backend.domain.lesson.service.LessonService;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;
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

    @GetMapping("")
    public ResponseEntity<PageResponseDTO<LessonListResDTO>> getList(PageRequestDTO dto){
        log.info("검색어 ={}, 검색종류={}, 정렬기준={}", dto.getKeyword(),dto.getType(), dto.getSort());
        return ResponseEntity.ok(lessonService.getAllLessonList(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonListResDTO> getOne(@PathVariable("id") Long id){
        return ResponseEntity.ok(lessonService.getOneLesson(id));
    }
}
