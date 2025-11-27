package com.semicolon.backend.domain.schedule.service;

import com.semicolon.backend.domain.faq.dto.FaqDTO;
import com.semicolon.backend.domain.schedule.dto.ScheduleDTO;
import com.semicolon.backend.domain.schedule.entity.Schedule;
import com.semicolon.backend.domain.schedule.repository.ScheduleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
@Service
public class ScheduleServiceImpl implements ScheduleService{

    @Autowired
    private ScheduleRepository repository;

    @Autowired
    private ModelMapper mapper;

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

    @Override
    public List<ScheduleDTO> getList() {
        return repository.findAll().stream().sorted(Comparator.comparing(Schedule::getStartDate)).map(i->mapper.map(i, ScheduleDTO.class)).toList();
    }

    @Override
    public void update(ScheduleDTO dto, Long id) {
        Schedule schedule = repository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("일치하는 일정이 존재하지 않습니다."));

        schedule.setTitle(dto.getTitle());
        schedule.setContent(dto.getContent());
        schedule.setStartDate(dto.getStartDate());
        schedule.setEndDate(dto.getEndDate());

        repository.save(schedule);
    }

    @Override
    public void register(ScheduleDTO dto) {
        Schedule schedule = Schedule.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        repository.save(schedule);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
