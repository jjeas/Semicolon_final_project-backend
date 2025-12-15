package com.semicolon.backend.domain.schedule.service;

import com.semicolon.backend.domain.schedule.dto.ScheduleDTO;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    public List<ScheduleDTO> findScheduleByDate(LocalDate start, LocalDate end);
    public PageResponseDTO<ScheduleDTO> getList(PageRequestDTO pageRequestDTO);
    public void update(ScheduleDTO dto, Long id);
    public void register(ScheduleDTO dto);
    public void delete(Long id);
    public List<ScheduleDTO> getSchedulesCurrentMonth();
}
