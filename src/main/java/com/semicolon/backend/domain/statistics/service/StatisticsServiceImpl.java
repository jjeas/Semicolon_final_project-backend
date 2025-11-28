package com.semicolon.backend.domain.statistics.service;

import com.semicolon.backend.domain.member.dto.MemberGenderAgeDTO;
import com.semicolon.backend.domain.member.entity.MemberRole;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService{

    private final MemberRepository memberRepository;

    @Override
    public Map<String, Object> getStats() {
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
}
