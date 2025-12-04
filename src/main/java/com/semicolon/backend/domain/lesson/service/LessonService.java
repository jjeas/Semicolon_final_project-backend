package com.semicolon.backend.domain.lesson.service;

import com.semicolon.backend.domain.lesson.dto.LessonReqDTO;

import java.util.List;

public interface LessonService {
    public void lessonReq(String loginIdFromToken, LessonReqDTO lessonReqDTO);
    public List<LessonReqDTO> getMyLessonList(String loginIdFromToken);
    List<LessonReqDTO> getAllLessonList();
}
