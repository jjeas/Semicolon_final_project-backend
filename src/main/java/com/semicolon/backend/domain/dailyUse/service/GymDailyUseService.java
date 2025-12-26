package com.semicolon.backend.domain.dailyUse.service;


import com.semicolon.backend.domain.dailyUse.dto.DailyUseDTO;
import com.semicolon.backend.domain.dailyUse.dto.GymDailyUseDTO;
import com.semicolon.backend.domain.dailyUse.entity.GymDailyUse;

import java.util.List;

public interface GymDailyUseService {

    public List<GymDailyUseDTO> getList(String loginIdFromToken);
    public void delete(Long id);
}
