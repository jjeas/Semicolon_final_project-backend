package com.semicolon.backend.global.reservationFilter;

import com.semicolon.backend.domain.dailyUse.repository.DailyUseRepository;
import com.semicolon.backend.domain.facility.dto.FacilitySpaceDTO;
import com.semicolon.backend.domain.facility.entity.FacilitySpace;
import com.semicolon.backend.domain.facility.repository.FacilitySpaceRepository;
import com.semicolon.backend.domain.lesson.dto.LessonReqDTO;
import com.semicolon.backend.domain.lesson.entity.LessonDay;
import com.semicolon.backend.domain.lesson.repository.LessonScheduleRepository;
import com.semicolon.backend.domain.rental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservationFilter {

    private final DailyUseRepository dailyUseRepository;
    private final LessonScheduleRepository lessonScheduleRepository;
    private final RentalRepository rentalRepository;
    private final FacilitySpaceRepository facilitySpaceRepository;
    private final ModelMapper mapper;

    public boolean isAvailable(Long spaceId, LocalDateTime start, LocalDateTime end){

        if (rentalRepository.isReserved(spaceId, start, end)>0) return false;

        if (dailyUseRepository.isReserved(spaceId, start, end)>0) return false;

        LessonDay lessonDayEnum = LessonDay.fromDayOfWeek(start.getDayOfWeek());
        String lessonDay = lessonDayEnum.name();

        if (lessonScheduleRepository.isLessonScheduled(
                spaceId,
                start.toLocalDate(),
                lessonDay,
                start.toLocalTime(),
                end.toLocalTime()
        )>0) return false;

        return true; // 통과!
    }

    public List<LocalTime> getAvailableTimes(Long spaceId, LocalDate date) {
        List<LocalTime> availableTimes = new ArrayList<>();
        LocalTime start = LocalTime.of(6, 0);
        LocalTime end = LocalTime.of(22, 0);

        while (!start.isAfter(end.minusHours(1))) {
            LocalDateTime startDateTime = LocalDateTime.of(date, start);
            LocalDateTime endDateTime = startDateTime.plusHours(1);

            // 모든 필터 통과한 시간만 추가
            if (isAvailable(spaceId, startDateTime, endDateTime)) {
                availableTimes.add(start);
            }

            start = start.plusHours(1);
        }

        return availableTimes;
    }

    public List<FacilitySpaceDTO> getAvailableFacility(Long facilityId, LessonReqDTO lessonReqDTO) {
        List<FacilitySpace> FacilitySpaceList = facilitySpaceRepository.findByFacilityId(facilityId);
        List<FacilitySpaceDTO> availableSpaces = new ArrayList<>();


        // 모든 필터 통과한 시간만 추가
        for(FacilitySpace i : FacilitySpaceList) {
            boolean allAvailable = true;
            LocalDate date = lessonReqDTO.getStartDate();

            while(!date.isAfter(lessonReqDTO.getEndDate())) {
                LessonDay toEng = LessonDay.fromDayOfWeek(date.getDayOfWeek());

                if(lessonReqDTO.getDays().stream()
                        .map(LessonDay::fromKorean)
                        .anyMatch(d -> d == toEng)) { // 요일 일치하는 날만 검사
                    LocalDateTime startDateTime = LocalDateTime.of(date, lessonReqDTO.getStartTime());
                    LocalDateTime endDateTime = LocalDateTime.of(date, lessonReqDTO.getEndTime());

                    if (!isAvailable(i.getId(), startDateTime, endDateTime)) {
                        allAvailable = false;
                        break;
                    }
                }
                date = date.plusDays(1);
            }
            if(allAvailable) {
                availableSpaces.add(mapper.map(i, FacilitySpaceDTO.class));
            }
        }
        return availableSpaces;
    }

    public List<Map<String, Object>> checkLessonTime(GetTimeReqDTO request) {

        List<Map<String, Object>> response = new ArrayList<>();
        List<FacilitySpace> spaces = facilitySpaceRepository.findByFacilityId(request.getFacilityId());

        for (FacilitySpace space : spaces) {

            List<Map<String, Object>> schedulePerDate = new ArrayList<>();
            LocalDate date = request.getStartDate();

            while (!date.isAfter(request.getEndDate())) {

                LessonDay lessonDay = LessonDay.fromDayOfWeek(date.getDayOfWeek());
                String koreanLabel = lessonDay.getLabel();

                if (request.getDays().contains(koreanLabel)) {

                    List<String> availableTimes = new ArrayList<>();
                    LocalTime time = LocalTime.of(6, 0);
                    LocalTime finish = LocalTime.of(22, 0);

                    while (!time.isAfter(finish.minusHours(1))) {
                        LocalDateTime startDt = LocalDateTime.of(date, time);
                        LocalDateTime endDt = startDt.plusHours(1);

                        if (isAvailable(space.getId(), startDt, endDt)) {
                            availableTimes.add(time.toString());
                        }

                        time = time.plusHours(1);
                    }

                    Map<String, Object> entry = new HashMap<>();
                    entry.put("date", date.toString());
                    entry.put("times", availableTimes);
                    schedulePerDate.add(entry);
                }

                date = date.plusDays(1);
            }

            Map<String, Object> spaceResult = new HashMap<>();
            spaceResult.put("spaceId", space.getId());
            spaceResult.put("spaceName", space.getSpaceName());
            spaceResult.put("schedule", schedulePerDate);

            response.add(spaceResult);
        }

        return response;
    }





}
