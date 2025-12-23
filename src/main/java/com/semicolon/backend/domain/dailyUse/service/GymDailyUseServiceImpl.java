package com.semicolon.backend.domain.dailyUse.service;

import com.semicolon.backend.domain.dailyUse.dto.DailyUseDTO;
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
import java.util.List;

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


//    @Override
//    public GymDailyUse register(String loginIdFromToken, GymDailyUseDTO dto) {
//
//        Member member = memberRepository.findByMemberLoginId(loginIdFromToken)
//                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원입니다."));
//
//        GymDailyUse gymDailyUse = GymDailyUse.builder()
//                .date(dto.getDate())
//                .createdAt(LocalDateTime.now())
//                .price(dto.getPrice())
//                .status(GymDailyUseStatus.RESERVED)
//                .member(member)
//                .build();
//
//        return gymDailyUseRepository.save(gymDailyUse);
//    }

    @Override
    public List<GymDailyUseDTO> getList(String loginIdFromToken) {
        Member member = memberRepository.findByMemberLoginId(loginIdFromToken)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 회원입니다."));
        return gymDailyUseRepository.findByMemberMemberId(member.getMemberId()).stream()
                .map(i-> GymDailyUseDTO.builder()
                        .id(i.getId())
                        .date(i.getDate())
                        .price(i.getPrice())
                        .build()).toList();
    }

    @Override
    public void delete(Long id) {
        GymDailyUse gym = gymDailyUseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약이 존재하지 않습니다."));

        gymDailyUseRepository.delete(gym);
    }
}
