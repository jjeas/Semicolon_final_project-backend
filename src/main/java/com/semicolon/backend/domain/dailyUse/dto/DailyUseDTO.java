package com.semicolon.backend.domain.dailyUse.dto;

import com.semicolon.backend.domain.facility.dto.FacilitySpaceDTO;
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
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String facilityName;
    private String spaceName;
    private long price;
    private Long spaceId;

}
