package com.semicolon.backend.domain.dailyUse.service;

import com.semicolon.backend.domain.dailyUse.dto.DailyUseDTO;
import com.semicolon.backend.domain.dailyUse.entity.DailyUse;
import com.semicolon.backend.domain.dailyUse.repository.DailyUseRepository;
import com.semicolon.backend.domain.facility.entity.FacilitySpace;
import com.semicolon.backend.domain.facility.repository.FacilitySpaceRepository;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.global.reservationFilter.ReservationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyUseServiceImpl implements DailyUseService{

    private final DailyUseRepository dailyUseRepository;
    private final MemberRepository memberRepository;
    private final FacilitySpaceRepository facilitySpaceRepository;
    private final ReservationFilter reservationFilter;

    @Override
    public DailyUse register(String loginIdFromToken ,DailyUseDTO dto) {

        boolean available = reservationFilter.isAvailable(dto.getSpaceId(),dto.getStartTime(),dto.getEndTime());
        if (!available) {
            throw new IllegalStateException("해당 시간에 이미 예약이 존재합니다.");
        }

        Member member = memberRepository.findByMemberLoginId(loginIdFromToken)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 회원입니다."));

        FacilitySpace facilitySpace = facilitySpaceRepository.findById(dto.getSpaceId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공간입니다."));

        DailyUse  dailyUse= DailyUse.builder()
                .space(facilitySpace)
                .member(member)
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();

        return dailyUseRepository.save(dailyUse);

    }
}
