package com.semicolon.backend.domain.statistics.service;

import com.semicolon.backend.domain.dailyUse.repository.DailyUseRepository;
import com.semicolon.backend.domain.dailyUse.repository.GymDailyUseRepository;
import com.semicolon.backend.domain.lesson.repository.LessonRepository;
import com.semicolon.backend.domain.member.dto.MemberGenderAgeDTO;
import com.semicolon.backend.domain.member.entity.MemberRole;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService{

    private final MemberRepository memberRepository;
    private final LessonRepository lessonRepository;
    private final DailyUseRepository dailyUseRepository;
    private final GymDailyUseRepository gymDailyUseRepository;

    @Override
    public Map<String, Object> getAgeGenderStats() {
        int year = LocalDate.now().getYear();
        List<MemberGenderAgeDTO> stats = memberRepository.getAgeGenderGroupStats(year);
        for(MemberGenderAgeDTO dto : stats){
            log.info("그룹:{} 성별:{} 인원:{}",dto.getAgeGroup(),dto.getGender(),dto.getCount());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("totalCount",memberRepository.countByMemberRole(MemberRole.ROLE_USER));
        result.put("stats",stats);
        return result;
    }

    @Override
    public Map<String, Object> getRegistrationLessonStats() {
        List<Object[]> rawData = lessonRepository.findPopularityStats();
        List<Map<String, Object>> stats = new ArrayList<>();
        long totalCnt = 0;
        for(Object[] raw:rawData){
            String category = (String) raw[0]; // 풋살 수영 등
            Long count = (Long) raw[1]; // 수강신청수
            totalCnt+=count; // 총 신청수
            Map<String, Object> statMap = new HashMap<>();
            statMap.put("category",category); //카테고리끼리 묶기
            statMap.put("count",count); //수끼리 묶기
            stats.add(statMap);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("totalCount",totalCnt);
        result.put("stats",stats);
        return result;
    }

    @Override
    public Map<String, Object> getDailyUseStats() {
        return Map.of();
    }
}
