package com.semicolon.backend.global.reservationFilter;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GetTimeReqDTO {
    private Long facilityId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> days;
}
