package com.semicolon.backend.domain.lesson.service;

import com.semicolon.backend.domain.facility.entity.FacilitySpace;
import com.semicolon.backend.domain.facility.repository.FacilityRepository;
import com.semicolon.backend.domain.facility.repository.FacilitySpaceRepository;
import com.semicolon.backend.domain.lesson.dto.LessonReqDTO;
import com.semicolon.backend.domain.lesson.entity.Lesson;
import com.semicolon.backend.domain.lesson.entity.LessonDay;
import com.semicolon.backend.domain.lesson.entity.LessonSchedule;
import com.semicolon.backend.domain.lesson.entity.LessonStatus;
import com.semicolon.backend.domain.lesson.repository.LessonRepository;
import com.semicolon.backend.domain.lesson.repository.LessonScheduleRepository;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.schedule.entity.Schedule;
import com.semicolon.backend.global.reservationFilter.ReservationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService{

    @Autowired
    private ModelMapper mapper;

    private final LessonRepository lessonRepository;
    private final MemberRepository memberRepository;
    private final FacilitySpaceRepository facilitySpaceRepository;
    private final ReservationFilter reservationFilter;

    @Override
    public void lessonReq(String loginIdFromToken, LessonReqDTO lessonReqDTO) {
        Member member = memberRepository.findByMemberLoginId(loginIdFromToken).orElseThrow();
        FacilitySpace facilitySpace = facilitySpaceRepository.findBySpaceRoomType(lessonReqDTO.getFacilityRoomType());

        LocalDate startDate = lessonReqDTO.getStartDate();
        LocalDate endDate = lessonReqDTO.getEndDate();

        List<LessonDay> days = lessonReqDTO.getDays().stream()
                .map(label ->
                        Arrays.stream(LessonDay.values())
                                .filter(i -> i.getLabel().equals(label))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("잘못된 요일: " + label))
                )
                .toList();

        // ★ 기간 전체에서 선택된 요일만 추출
        List<LocalDate> targetDays = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LessonDay current = LessonDay.fromDayOfWeek(date.getDayOfWeek());
            if (days.contains(current)) {
                targetDays.add(date);
            }
        }

    // ★ targetDays 의 모든 날짜에 대해 예약 가능 여부 검사
        for (LocalDate d : targetDays) {
            LocalDateTime s = LocalDateTime.of(d, lessonReqDTO.getStartTime());
            LocalDateTime e = LocalDateTime.of(d, lessonReqDTO.getEndTime());

            if (!reservationFilter.isAvailable(facilitySpace.getId(), s, e)) {
                throw new IllegalArgumentException("해당 요일/시간에 공간이 비어있지 않습니다: " + d);
            }
        }

        LessonSchedule lessonSchedule = LessonSchedule.builder()
                .lessonDay(days)
                .startTime(lessonReqDTO.getStartTime())
                .endTime(lessonReqDTO.getEndTime())
                .build();

        Lesson lesson = Lesson.builder()
                .title(lessonReqDTO.getTitle())
                .startDate(lessonReqDTO.getStartDate())
                .endDate(lessonReqDTO.getEndDate())
                .level(lessonReqDTO.getLevel())
                .description(lessonReqDTO.getDescription())
                .tools(lessonReqDTO.getTools())
                .memo(lessonReqDTO.getMemo())
                .curriculum(lessonReqDTO.getCurriculum())
                .minPeople(lessonReqDTO.getMinPeople())
                .maxPeople(lessonReqDTO.getMaxPeople())
                .partnerId(member)
                .lessonStatus(LessonStatus.PENDING)
                .facilitySpace(facilitySpace)
                .build();

        lesson.toList(lessonSchedule);

        lessonRepository.save(lesson);
    }

    @Override
    public List<LessonReqDTO> getMyLessonList(String loginIdFromToken) {
        Member member = memberRepository.findByMemberLoginId(loginIdFromToken)
                .orElseThrow(() -> new NoSuchElementException("해당 ID에 해당되는 회원이 없습니다."));

        List<Lesson> lessons = lessonRepository.findByPartnerId(member);

                return lessons.stream()
                        .map((i) -> LessonReqDTO.builder()
                        .partnerId(member.getMemberId())
                        .partnerName(member.getMemberName())
                        .title(i.getTitle())
                        .startDate(i.getStartDate())
                        .endDate(i.getEndDate())
                        .days(i.getSchedules().stream().flatMap(j -> j.getLessonDay().stream()).map(LessonDay::getLabel).toList())
                        .startTime(i.getSchedules().get(0).getStartTime())
                        .endTime(i.getSchedules().get(0).getEndTime())
                        .level(i.getLevel())
                        .description(i.getDescription())
                        .tools(i.getTools())
                        .memo(i.getMemo())
                        .curriculum(i.getCurriculum())
                        .minPeople(i.getMinPeople())
                        .maxPeople(i.getMaxPeople())
                        .LessonStatus(i.getLessonStatus().name())
                        .facilityType(i.getFacilitySpace().getFacility().getFacilityType())
                        .facilityRoomType(i.getFacilitySpace().getSpaceRoomType())
                        .build()).toList();
    }

    @Override
    public List<LessonReqDTO> getAllLessonList() {
        return lessonRepository.findAll().stream().map(lesson -> LessonReqDTO.builder()
                .partnerId(lesson.getPartnerId().getMemberId())
                .partnerName(lesson.getPartnerId().getMemberName())
                .title(lesson.getTitle())
                .startDate(lesson.getStartDate())
                .endDate(lesson.getEndDate())
                .days(lesson.getSchedules().stream().flatMap(j -> j.getLessonDay().stream()).map(LessonDay::getLabel).toList())
                .startTime(lesson.getSchedules().get(0).getStartTime())
                .endTime(lesson.getSchedules().get(0).getEndTime())
                .level(lesson.getLevel())
                .description(lesson.getDescription())
                .tools(lesson.getTools())
                .memo(lesson.getMemo())
                .curriculum(lesson.getCurriculum())
                .minPeople(lesson.getMinPeople())
                .maxPeople(lesson.getMaxPeople())
                .LessonStatus(lesson.getLessonStatus().name())
                .facilityType(lesson.getFacilitySpace().getFacility().getFacilityType())
                .facilityRoomType(lesson.getFacilitySpace().getSpaceRoomType()).build())
                .toList();
    }
}
