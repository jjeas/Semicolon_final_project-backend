package com.semicolon.backend.domain.dailyUse.service;

import com.semicolon.backend.domain.dailyUse.dto.GymDailyUseDTO;
import com.semicolon.backend.domain.dailyUse.entity.GymDailyUse;
import com.semicolon.backend.domain.dailyUse.entity.GymDailyUseStatus;
import com.semicolon.backend.domain.dailyUse.repository.GymDailyUseRepository;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class GymDailyUseServiceImpl implements GymDailyUseService{

    private final GymDailyUseRepository gymDailyUseRepository;
    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateExpiredReservations() {
        gymDailyUseRepository.updateExpired(LocalDate.now());
    }


    @Override
    public GymDailyUse register(String loginIdFromToken, GymDailyUseDTO dto) {

        Member member = memberRepository.findByMemberLoginId(loginIdFromToken)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원입니다."));

        GymDailyUse gymDailyUse = GymDailyUse.builder()
                .date(dto.getDate())
                .createdAt(LocalDateTime.now())
                .status(GymDailyUseStatus.RESERVED)
                .member(member)
                .build();

        return gymDailyUseRepository.save(gymDailyUse);
    }
}
