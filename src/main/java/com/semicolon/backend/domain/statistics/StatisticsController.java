package com.semicolon.backend.domain.statistics;

import com.semicolon.backend.domain.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/stat")
@RequiredArgsConstructor
@Slf4j
public class StatisticsController {
    private final StatisticsService service;

    @GetMapping("/ageGender")
    public ResponseEntity<Map<String, Object>> getAgeGenderStats(){
        log.info("연령대별 성비 가져오기");
        return ResponseEntity.ok(service.getStats());
    }
}
