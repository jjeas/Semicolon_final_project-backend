package com.semicolon.backend.domain.lesson;

import com.semicolon.backend.domain.lesson.dto.LessonListResDTO;
import com.semicolon.backend.domain.lesson.dto.LessonReqDTO;
import com.semicolon.backend.domain.lesson.dto.LessonStatusDTO;
import com.semicolon.backend.domain.lesson.entity.LessonStatus;
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
    public ResponseEntity<PageResponseDTO<LessonListResDTO>> getList(@AuthenticationPrincipal String loginId, PageRequestDTO dto){
        log.info("검색어 ={}, 검색종류={}, 정렬기준={}", dto.getKeyword(),dto.getType(), dto.getSort());
        return ResponseEntity.ok(lessonService.getAllLessonList(dto,loginId));
    }

    @GetMapping("/admin")
    public ResponseEntity<PageResponseDTO<LessonListResDTO>> adminGetList(PageRequestDTO dto){
        return ResponseEntity.ok(lessonService.adminGetAllLessonList(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonListResDTO> getOne(@PathVariable("id") Long id, @AuthenticationPrincipal String loginId){
        return ResponseEntity.ok(lessonService.getOneLesson(id,loginId));
    }

    @PostMapping("/status/{lessonId}")
    public ResponseEntity<String> changeStatus(@RequestBody LessonStatusDTO dto){
        lessonService.changeStatus(dto);
        return ResponseEntity.ok("성공");
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<LessonListResDTO> getOne(@PathVariable("id") Long id) {
        return ResponseEntity.ok(lessonService.adminGetOneLesson(id));
    }
      
    @GetMapping("/myLessons/search")
    public ResponseEntity<List<LessonReqDTO>> searchLessons(@AuthenticationPrincipal String loginIdFromToken, @RequestParam String title){
        List<LessonReqDTO> dto = lessonService.searchLessonsByTitle(loginIdFromToken, title);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/myLessons/{lessonNo}")
    public ResponseEntity<LessonReqDTO> getMyOneLesson(@AuthenticationPrincipal String loginIdFromToken, @PathVariable("lessonNo") Long lessonId) {
        LessonReqDTO dto = lessonService.getMyOneLesson(loginIdFromToken, lessonId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/preview")
    public ResponseEntity<List<LessonListResDTO>> previewLesson(){
        return ResponseEntity.ok(lessonService.getPreviewLesson());
    }
}
