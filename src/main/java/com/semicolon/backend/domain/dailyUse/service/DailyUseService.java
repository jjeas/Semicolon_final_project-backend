package com.semicolon.backend.domain.dailyUse.service;

import com.semicolon.backend.domain.dailyUse.dto.DailyUseDTO;
import com.semicolon.backend.domain.dailyUse.entity.DailyUse;

import java.util.List;

public interface DailyUseService {
    public DailyUse register(String loginIdFromToken, DailyUseDTO dto);
    public List<DailyUseDTO> getList(String loginIdFromToken);
    public void delete(Long id);
}
