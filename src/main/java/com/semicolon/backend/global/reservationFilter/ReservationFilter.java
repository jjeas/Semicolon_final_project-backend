package com.semicolon.backend.global.reservationFilter;

import com.semicolon.backend.domain.dailyUse.repository.DailyUseRepository;
import com.semicolon.backend.domain.lesson.entity.LessonDay;
import com.semicolon.backend.domain.lesson.repository.LessonScheduleRepository;
import com.semicolon.backend.domain.rental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationFilter {

    private final DailyUseRepository dailyUseRepository;
    private final LessonScheduleRepository lessonScheduleRepository;
    private final RentalRepository rentalRepository;

    public boolean isAvailable(Long spaceId, LocalDateTime start, LocalDateTime end){

        // 1. 대관이랑 겹쳐?
        if (rentalRepository.isReserved(spaceId, start, end)>0) return false;

        // 2. 일일이용이랑 겹쳐?
        if (dailyUseRepository.isReserved(spaceId, start, end)>0) return false;

        LessonDay lessonDayEnum = LessonDay.fromDayOfWeek(start.getDayOfWeek());
        String lessonDay = lessonDayEnum.name();

        // 3. 강의랑 겹쳐? (날짜/요일/시간 분해해서 전달)
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
        LocalTime end = LocalTime.of(22, 0); // 마지막 시작 시간 18:00

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

}
