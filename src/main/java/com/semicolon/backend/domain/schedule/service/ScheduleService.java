package com.semicolon.backend.domain.schedule.service;

import com.semicolon.backend.domain.schedule.dto.ScheduleDTO;
import com.semicolon.backend.domain.schedule.entity.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    public List<ScheduleDTO> findScheduleByDate(LocalDate start, LocalDate end);
}
