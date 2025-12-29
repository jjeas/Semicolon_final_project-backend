package com.semicolon.backend.domain.lesson.service;

import com.semicolon.backend.domain.facility.entity.FacilitySpace;
import com.semicolon.backend.domain.facility.repository.FacilitySpaceRepository;
import com.semicolon.backend.domain.lesson.dto.LessonListResDTO;
import com.semicolon.backend.domain.lesson.dto.LessonReqDTO;
import com.semicolon.backend.domain.lesson.dto.LessonStatusDTO;
import com.semicolon.backend.domain.lesson.entity.Lesson;
import com.semicolon.backend.domain.lesson.entity.LessonDay;
import com.semicolon.backend.domain.lesson.entity.LessonSchedule;
import com.semicolon.backend.domain.lesson.entity.LessonStatus;
import com.semicolon.backend.domain.lesson.repository.LessonRepository;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.registration.entity.RegistrationStatus;
import com.semicolon.backend.domain.registration.repository.RegistrationRepository;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;
import com.semicolon.backend.global.reservationFilter.ReservationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final RegistrationRepository registrationRepository;

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
                        .currentPeople(i.getCurrentPeople())
                        .lessonNo(i.getId())
                        .build()).toList();
    }

    @Override
    public PageResponseDTO<LessonListResDTO> getAllLessonList(PageRequestDTO dto, String loginId) {
        Sort sort = Sort.by("id").descending();
        if(dto.getSort()!=null){
            switch (dto.getSort()){
                case "LATEST" : sort = Sort.by("startDate").ascending();
                break;
                case "FASTEST" : sort = Sort.by("startDate").descending();
            }
        }
        Pageable pageable = PageRequest.of(dto.getPage()-1, dto.getSize(), sort);
        String titleKeyword = null;
        String partnerKeyword = null;
        String keyword = dto.getKeyword();
        if(keyword!=null && !keyword.isEmpty()){
            if("t".equals(dto.getType())){
                titleKeyword=dto.getKeyword();
            } else if ("c".equals(dto.getType())) {
                partnerKeyword=dto.getKeyword();
            }else{
                titleKeyword = dto.getKeyword();
                partnerKeyword = dto.getKeyword();
            }
        }
        List<LessonDay> targetDays = new ArrayList<>();
        if(dto.getDays()!=null && !dto.getDays().isEmpty()){
            targetDays = dto.getDays().stream().map(i->LessonDay.valueOf(i)).toList();
        }else{
            targetDays=Arrays.asList(LessonDay.values());
        }

        String startTimeStr = null;
        String endTimeStr = null;

        if (dto.getStartTime() != null) {
            startTimeStr = dto.getStartTime().toString(); // "10:00" 문자열이 됨
        }
        if (dto.getEndTime() != null) {
            endTimeStr = dto.getEndTime().toString();     // "13:00" 문자열이 됨
        }
        Page<Lesson> result = lessonRepository.searchLesson(
                dto.getCategory(),
                titleKeyword,
                partnerKeyword,
                targetDays,
                startTimeStr, // DTO 값 그대로 전달
                endTimeStr,   // DTO 값 그대로 전달
                dto.getAvailable(),
                loginId,
                pageable
        );
        List<Long> myRegisteredIds = new ArrayList<>();
        if(loginId != null && !loginId.isEmpty()){
            // 로그인한 유저가 신청한 레슨 ID 목록 가져오기
            myRegisteredIds = registrationRepository.findRegisteredLessonId(loginId);
        }
        List<Long> finalList = myRegisteredIds;
        List<LessonListResDTO> dtoList=result.getContent().stream().map(lesson ->{
            Long current=registrationRepository.countByLesson_IdAndStatus(lesson.getId(), RegistrationStatus.APPLIED);
            LocalDate end = lesson.getStartDate().minusDays(3);
                    LessonListResDTO resDTO= LessonListResDTO.builder()
                        .lessonId(lesson.getId())
                        .status(lesson.getLessonStatus())
                        .level(lesson.getLevel())
                        .description(lesson.getDescription())
                        .category(lesson.getFacilitySpace().getFacility().getFacilityName())
                        .startDate(lesson.getStartDate())
                        .endDate(lesson.getEndDate())
                        .title(lesson.getTitle())
                        .partnerName(lesson.getPartnerId().getMemberName())
                        .facilityType(lesson.getFacilitySpace().getSpaceName())
                        .startTime(lesson.getSchedules().get(0).getStartTime())
                        .endTime(lesson.getSchedules().get(0).getEndTime())
                        .days(lesson.getSchedules().get(0).getLessonDay().stream().map(
                lessonDay -> lessonDay.getLabel()).toList())
                        .isRegistered(finalList.contains(lesson.getId()))
                        .maxPeople(lesson.getMaxPeople())
                        .minPeople(lesson.getMinPeople())
                        .currentPeople(current)
                        .regEndDate(end)
                        .price(lesson.getPrice())
                        .build();
                    resDTO.checkEndDate();
                    return resDTO;
        })
                        .toList();

        return PageResponseDTO.<LessonListResDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(dto)
                .totalCnt(result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<LessonListResDTO> adminGetAllLessonList(PageRequestDTO dto) {

        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize());

        String keyword = dto.getKeyword();
        String type = dto.getType();
        String role = dto.getRole();

        LessonStatus status = null;
        if (role != null && !role.isEmpty()) {
            status = LessonStatus.valueOf(role);
        }

        String titleKeyword = null;
        String partnerKeyword = null;

        if (keyword != null && !keyword.isEmpty()) {
            if ("lessonName".equals(type)) {
                titleKeyword = keyword;
            } else if ("name".equals(type)) {
                partnerKeyword = keyword;
            } else {
                titleKeyword = keyword;
                partnerKeyword = keyword;
            }
        }

        Page<Lesson> result = lessonRepository.searchAdminLessonWithSort(
                status,
                titleKeyword,
                partnerKeyword,
                pageable
        );

        List<LessonListResDTO> dtoList = result.getContent().stream().map(lesson ->
                LessonListResDTO.builder()
                        .lessonId(lesson.getId())
                        .status(lesson.getLessonStatus())
                        .level(lesson.getLevel())
                        .category(lesson.getFacilitySpace().getFacility().getFacilityName())
                        .startDate(lesson.getStartDate())
                        .endDate(lesson.getEndDate())
                        .title(lesson.getTitle())
                        .partnerName(lesson.getPartnerId().getMemberName())
                        .facilityType(lesson.getFacilitySpace().getSpaceName())
                        .startTime(lesson.getSchedules().get(0).getStartTime())
                        .endTime(lesson.getSchedules().get(0).getEndTime())
                        .days(lesson.getSchedules().get(0).getLessonDay()
                                .stream()
                                .map(day -> day.getLabel()).toList())
                        .build()
        ).toList();

        return PageResponseDTO.<LessonListResDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(dto)
                .totalCnt(result.getTotalElements())
                .build();
    }


    @Override
    public LessonListResDTO adminGetOneLesson(Long id) {
        Lesson lesson =lessonRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 id에 일치하는 강의가 없습니다."));
        return LessonListResDTO.builder()
                .lessonId(lesson.getId())
                .status(lesson.getLessonStatus())
                .level(lesson.getLevel())
                .category(lesson.getFacilitySpace().getFacility().getFacilityName())
                .startDate(lesson.getStartDate())
                .endDate(lesson.getEndDate())
                .title(lesson.getTitle())
                .price(lesson.getPrice())
                .partnerName(lesson.getPartnerId().getMemberName())
                .facilityType(lesson.getFacilitySpace().getSpaceName())
                .startTime(lesson.getSchedules().get(0).getStartTime())
                .endTime(lesson.getSchedules().get(0).getEndTime())
                .days(lesson.getSchedules().get(0).getLessonDay().stream().map(
                        lessonDay -> lessonDay.getLabel()).toList())
                .build();
    }

    @Override
    public LessonListResDTO getOneLesson(Long id, String loginId) {
        Lesson lesson =lessonRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 id에 일치하는 강의가 없습니다."));
        List<Long> myRegisteredIds = new ArrayList<>();
        if(loginId!=null && !loginId.isEmpty()){
            myRegisteredIds = registrationRepository.findRegisteredLessonId(loginId);
        }
        List<Long> finalList = myRegisteredIds;
        Long current=registrationRepository.countByLesson_IdAndStatus(lesson.getId(), RegistrationStatus.APPLIED);
        LocalDate end = lesson.getStartDate().minusDays(3);
        return LessonListResDTO.builder()
                .price(lesson.getPrice())
                .description(lesson.getDescription())
                .lessonId(lesson.getId())
                .status(lesson.getLessonStatus())
                .level(lesson.getLevel())
                .category(lesson.getFacilitySpace().getFacility().getFacilityName())
                .startDate(lesson.getStartDate())
                .endDate(lesson.getEndDate())
                .title(lesson.getTitle())
                .partnerName(lesson.getPartnerId().getMemberName())
                .facilityType(lesson.getFacilitySpace().getSpaceName())
                .startTime(lesson.getSchedules().get(0).getStartTime())
                .endTime(lesson.getSchedules().get(0).getEndTime())
                .days(lesson.getSchedules().get(0).getLessonDay().stream().map(
                        lessonDay -> lessonDay.getLabel()).toList())
                .isRegistered(finalList.contains(lesson.getId()))
                .maxPeople(lesson.getMaxPeople())
                .minPeople(lesson.getMinPeople())
                .regEndDate(end)
                .currentPeople(current)
                .build();
    }

    @Override
    public List<LessonReqDTO> searchLessonsByTitle(String loginIdFromToken, String title) {
        Member member = memberRepository.findByMemberLoginId(loginIdFromToken)
                .orElseThrow(() -> new NoSuchElementException("해당 ID에 해당되는 회원이 없습니다."));

        List<Lesson> lesson = lessonRepository.searchByTitle(loginIdFromToken, title);

        return lesson.stream()
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
                        .currentPeople(i.getCurrentPeople())
                        .lessonNo(i.getId())
                        .build()).toList();
    }

    @Override
    public LessonReqDTO getMyOneLesson(String loginIdFromToken, Long lessonId) {
        Member member = memberRepository.findByMemberLoginId(loginIdFromToken)
                .orElseThrow(() -> new NoSuchElementException("해당 ID에 해당되는 회원이 없습니다."));

        Lesson lesson = lessonRepository.findLessonByPartnerAndId(loginIdFromToken, lessonId);

        if (lesson == null) {
            throw new NoSuchElementException("없는 강좌입니다.");
        }

        return LessonReqDTO.builder()
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
                .facilityRoomType(lesson.getFacilitySpace().getSpaceRoomType())
                .currentPeople(lesson.getCurrentPeople())
                .lessonNo(lesson.getId())
                .build();


    }

    @Override
    public List<LessonListResDTO> getPreviewLesson() {
        LocalDate today = LocalDate.now();
        List<Lesson> lessons = lessonRepository.findTop7ByStartDateAfterAndLessonStatusOrderByStartDateAsc(today, LessonStatus.ACCEPTED);

        return lessons.stream().map(lesson -> {
            LocalDate calculatedRegEndDate = lesson.getStartDate().minusDays(3);
            Long current = registrationRepository.countByLesson_IdAndStatus(lesson.getId(), RegistrationStatus.APPLIED);
            return LessonListResDTO.builder()
                    .lessonId(lesson.getId())
                    .title(lesson.getTitle())
                    .partnerName(lesson.getPartnerId().getMemberName())
                    .category(lesson.getFacilitySpace().getFacility().getFacilityName())
                    .status(lesson.getLessonStatus())
                    .startDate(lesson.getStartDate())
                    .endDate(lesson.getEndDate())
                    .startTime(lesson.getSchedules().isEmpty() ? null : lesson.getSchedules().get(0).getStartTime())
                    .endTime(lesson.getSchedules().isEmpty() ? null : lesson.getSchedules().get(0).getEndTime())
                    .regEndDate(calculatedRegEndDate)
                    .currentPeople(current)
                    .maxPeople(lesson.getMaxPeople())
                    .build();
        }).toList();
    }

    @Override
    public void changeStatus(LessonStatusDTO dto) {
        Lesson lesson = lessonRepository.findById(dto.getId()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 강의입니다."));
        lesson.setPrice(dto.getPrice());
        lesson.setLessonStatus(dto.getStatus());
        lessonRepository.save(lesson);
    }


}
