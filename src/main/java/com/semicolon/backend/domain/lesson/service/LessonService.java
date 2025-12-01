package com.semicolon.backend.domain.lesson.service;

import com.semicolon.backend.domain.lesson.dto.LessonReqDTO;

public interface LessonService {
    public void lessonReq(String loginIdFromToken, LessonReqDTO lessonReqDTO);
}
