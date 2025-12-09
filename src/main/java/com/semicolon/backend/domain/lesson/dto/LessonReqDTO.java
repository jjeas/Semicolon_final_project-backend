package com.semicolon.backend.domain.lesson.dto;

import com.semicolon.backend.domain.facility.entity.SpaceRoomType;
import com.semicolon.backend.domain.facility.entity.FacilityType;
import com.semicolon.backend.domain.lesson.entity.LessonDay;
import com.semicolon.backend.domain.lesson.entity.LessonStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LessonReqDTO {
    private long partnerId;
    private long lessonNo;
    private String partnerName;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> days;
    private LocalTime startTime;
    private LocalTime endTime;
    private String level;
    private String description;
    private String tools;
    private String memo;
    private String curriculum;
    private String LessonStatus;
    private long minPeople;
    private long maxPeople;
    private FacilityType facilityType;
    private SpaceRoomType facilityRoomType;
    private long currentPeople;

}
