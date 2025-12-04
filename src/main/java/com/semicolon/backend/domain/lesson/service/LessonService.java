package com.semicolon.backend.domain.lesson.service;

import com.semicolon.backend.domain.lesson.dto.LessonListResDTO;
import com.semicolon.backend.domain.lesson.dto.LessonReqDTO;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;

import java.util.List;

public interface LessonService {
    public void lessonReq(String loginIdFromToken, LessonReqDTO lessonReqDTO);
    public List<LessonReqDTO> getMyLessonList(String loginIdFromToken);
    PageResponseDTO<LessonListResDTO> getAllLessonList(PageRequestDTO dto);
    LessonListResDTO getOneLesson(Long id);
}
