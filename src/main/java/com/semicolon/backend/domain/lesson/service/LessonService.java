package com.semicolon.backend.domain.lesson.service;

import com.semicolon.backend.domain.lesson.dto.LessonListResDTO;
import com.semicolon.backend.domain.lesson.dto.LessonReqDTO;
import com.semicolon.backend.domain.lesson.dto.LessonStatusDTO;
import com.semicolon.backend.domain.lesson.entity.LessonStatus;
import com.semicolon.backend.domain.partner.entity.PartnerStatus;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;

import java.util.List;

public interface LessonService {
    public void lessonReq(String loginIdFromToken, LessonReqDTO lessonReqDTO);
    public List<LessonReqDTO> getMyLessonList(String loginIdFromToken);
    PageResponseDTO<LessonListResDTO> adminGetAllLessonList(PageRequestDTO dto);
    LessonListResDTO adminGetOneLesson(Long id);
    public void changeStatus(LessonStatusDTO dto);
    PageResponseDTO<LessonListResDTO> getAllLessonList(PageRequestDTO dto, String loginId);
    LessonListResDTO getOneLesson(Long id,String loginId);
    List<LessonReqDTO> searchLessonsByTitle(String loginIdFromToken, String title);
    LessonReqDTO getMyOneLesson(String loginIdFromToken, Long lessonId);
    List<LessonListResDTO> getPreviewLesson();
}
