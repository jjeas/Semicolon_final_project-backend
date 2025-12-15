package com.semicolon.backend.domain.admin.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DashboardCountDTO {
    long memberCnt;
    long reservationCnt;
    long supportCnt;
}
