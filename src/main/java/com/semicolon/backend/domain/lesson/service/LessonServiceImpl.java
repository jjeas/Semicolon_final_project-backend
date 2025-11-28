package com.semicolon.backend.domain.lesson.service;

import com.semicolon.backend.domain.facility.entity.FacilitySpace;
import com.semicolon.backend.domain.facility.repository.FacilityRepository;
import com.semicolon.backend.domain.facility.repository.FacilitySpaceRepository;
import com.semicolon.backend.domain.lesson.dto.LessonReqDTO;
import com.semicolon.backend.domain.lesson.entity.Lesson;
import com.semicolon.backend.domain.lesson.entity.LessonSchedule;
import com.semicolon.backend.domain.lesson.entity.LessonStatus;
import com.semicolon.backend.domain.lesson.repository.LessonRepository;
import com.semicolon.backend.domain.lesson.repository.LessonScheduleRepository;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.schedule.entity.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService{

    private final LessonRepository lessonRepository;
    private final MemberRepository memberRepository;
    private final FacilitySpaceRepository facilitySpaceRepository;

    @Override
    public void lessonReq(String loginIdFromToken, LessonReqDTO lessonReqDTO) {
        Member member = memberRepository.findByMemberLoginId(loginIdFromToken).orElseThrow();
        FacilitySpace facilitySpace = facilitySpaceRepository.findBySpaceRoomType(lessonReqDTO.getFacilityRoomType());

        LessonSchedule lessonSchedule = LessonSchedule.builder()
                .lessonDay(lessonReqDTO.getDays())
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
}
