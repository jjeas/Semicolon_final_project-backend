package com.semicolon.backend.domain.admin.service;

import com.semicolon.backend.domain.admin.dto.DashboardCountDTO;
import com.semicolon.backend.domain.admin.dto.DashboardDTO;
import com.semicolon.backend.domain.dailyUse.repository.DailyUseRepository;
import com.semicolon.backend.domain.dailyUse.repository.GymDailyUseRepository;
import com.semicolon.backend.domain.lesson.entity.Lesson;
import com.semicolon.backend.domain.lesson.repository.LessonRepository;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.partner.entity.Partner;
import com.semicolon.backend.domain.partner.repository.PartnerRepository;
import com.semicolon.backend.domain.rental.entity.Rental;
import com.semicolon.backend.domain.rental.repository.RentalRepository;
import com.semicolon.backend.domain.support.repository.SupportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final LessonRepository lessonRepository;
    private final RentalRepository rentalRepository;
    private final DailyUseRepository dailyUseRepository;
    private final GymDailyUseRepository gymDailyUseRepository;
    private final SupportRepository supportRepository;
    private final MemberRepository memberRepository;
    private final PartnerRepository partnerRepository;

    @Override
    public List<DashboardDTO> getList() {
        List<DashboardDTO> pendingList = new ArrayList<>();
        rentalRepository.findByStatusIsPending().forEach(i -> {
            DashboardDTO dto = DashboardDTO.builder()
                    .id(i.getId())
                    .title(i.getSpace().getFacility().getFacilityName() + " · " + i.getSpace().getSpaceName() + " 대관 신청")
                    .name(i.getName())
                    .type("대관신청")
                    .date(i.getCreatedAt().toLocalDate())
                    .build();
            pendingList.add(dto);
        });
        lessonRepository.findByStatusIsPending().forEach(i->{
            DashboardDTO dto = DashboardDTO.builder()
                    .id(i.getId())
                    .title(i.getTitle()+" 개설 신청")
                    .name(i.getPartnerId().getMemberName())
                    .type("강좌개설")
                    .date(i.getStartDate())
                    .build();
            pendingList.add(dto);
        });
        partnerRepository.findByStatusIsPending().forEach(i->{
            DashboardDTO dto = DashboardDTO.builder()
                    .id(i.getRequestNo())
                    .title(i.getMember().getMemberName()+ " 강사 승급 신청")
                    .name(i.getMember().getMemberName())
                    .type("파트너")
                    .date(i.getRequestDate().toLocalDate())
                    .build();
            pendingList.add(dto);
        });

        return pendingList;
    }

    @Override
    public DashboardCountDTO getCount() {
        return DashboardCountDTO.builder()
                .memberCnt(memberRepository.countMembersJoinToday())
                .reservationCnt(dailyUseRepository.countDailyUseToday()+ gymDailyUseRepository.countGymDailyUseToday()+rentalRepository.countRentalToday())
                .supportCnt(supportRepository.countStatusIsWaiting())
                .build();
    }
}
