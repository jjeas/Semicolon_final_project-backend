package com.semicolon.backend.domain.rental.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RentalDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
