package com.semicolon.backend.domain.schedule.service;

import com.semicolon.backend.domain.schedule.dto.ScheduleDTO;
import com.semicolon.backend.domain.schedule.entity.Schedule;
import com.semicolon.backend.domain.schedule.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class ScheduleServiceImpl implements ScheduleService{

    @Autowired
    private ScheduleRepository repository;

    @Override
    public List<ScheduleDTO> findScheduleByDate(LocalDate start, LocalDate end) {
        return repository.findDateBetween(start, end).stream().map(schedule ->
                ScheduleDTO.builder()
                        .scheduleId(schedule.getScheduleId())
                        .title(schedule.getTitle())
                        .content(schedule.getContent())
                        .title(schedule.getTitle())
                        .startDate(schedule.getStartDate())
                        .endDate(schedule.getEndDate())
                        .build()).toList();
    }
}
