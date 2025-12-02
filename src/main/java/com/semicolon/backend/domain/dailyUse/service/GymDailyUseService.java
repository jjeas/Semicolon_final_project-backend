package com.semicolon.backend.domain.dailyUse.service;


import com.semicolon.backend.domain.dailyUse.dto.GymDailyUseDTO;
import com.semicolon.backend.domain.dailyUse.entity.GymDailyUse;

public interface GymDailyUseService {

    public GymDailyUse register(String loginIdFromToken, GymDailyUseDTO dto);
}
