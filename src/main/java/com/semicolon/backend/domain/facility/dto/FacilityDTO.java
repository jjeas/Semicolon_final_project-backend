package com.semicolon.backend.domain.facility.dto;

import com.semicolon.backend.domain.facility.entity.FacilityType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FacilityDTO {

    private Long id;
    private String facilityName;
    private FacilityType facilityType;
    private boolean dailyUseAvailable;

}
