package com.semicolon.backend.domain.dailyUse.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GymDailyUseDTO {
    private Long id;
    private LocalDate date;
    private long price;
}
