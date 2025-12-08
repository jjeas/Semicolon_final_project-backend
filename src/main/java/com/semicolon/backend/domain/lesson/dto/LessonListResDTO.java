package com.semicolon.backend.domain.lesson.dto;

import com.semicolon.backend.domain.lesson.entity.LessonStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonListResDTO {
    private Long lessonId;
    private String title;
    private String partnerName;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> days;
    private LocalTime startTime;
    private LocalTime endTime;
    private String level;
    private String facilityType;
    private LessonStatus status;
    private boolean isRegistered;
    private String description;

    private Long maxPeople;
    private Long currentPeople;
}
