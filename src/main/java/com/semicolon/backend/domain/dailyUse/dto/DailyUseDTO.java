package com.semicolon.backend.domain.dailyUse.dto;

import com.semicolon.backend.domain.dailyUse.entity.DailyUseStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DailyUseDTO {
    private Long id;
    private LocalDate useDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private DailyUseStatus useStatus;

}
