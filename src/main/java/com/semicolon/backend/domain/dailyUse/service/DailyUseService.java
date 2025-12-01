package com.semicolon.backend.domain.dailyUse.service;

import com.semicolon.backend.domain.dailyUse.dto.DailyUseDTO;
import com.semicolon.backend.domain.dailyUse.entity.DailyUse;

public interface DailyUseService {
    public DailyUse register(String loginIdFromToken, DailyUseDTO dto);
}
