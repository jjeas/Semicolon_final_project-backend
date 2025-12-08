package com.semicolon.backend.domain.attendance.service;

import com.semicolon.backend.domain.attendance.dto.AttendanceDTO;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    public List<AttendanceDTO> getList(String loginIdFromToken, Long lessonNo, LocalDate date);
    public void save(String loginIdFromToken, List<AttendanceDTO> requestList, Long lessonNo);
}
