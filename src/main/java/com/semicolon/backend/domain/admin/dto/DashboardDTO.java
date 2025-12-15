package com.semicolon.backend.domain.admin.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DashboardDTO {
    private long id;
    private String title;
    private String name;
    private String type;
    private LocalDate date;


}
