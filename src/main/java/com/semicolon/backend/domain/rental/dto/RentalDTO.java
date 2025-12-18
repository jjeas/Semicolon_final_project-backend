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
    private Long spaceId;
    private String facilityName;
    private String spaceName;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Long price;

    private String name;
    private String phoneNumber;
    private String memo;

    private String status;
}
