package com.semicolon.backend.domain.statistics.service;

import java.util.Map;

public interface StatisticsService {
    Map<String, Object> getAgeGenderStats();
    Map<String, Object> getRegistrationLessonStats();
    Map<String, Object> getDailyUseStats();
}
