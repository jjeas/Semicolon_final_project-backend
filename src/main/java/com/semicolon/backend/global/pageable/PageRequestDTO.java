package com.semicolon.backend.global.pageable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageRequestDTO {
    private int page = 1;
    private int size = 10;
    private String keyword;
    private String type;
    private String role;
    private String status;
    private String sort;
    private String category;
    private List<String> days;
    private LocalDate startDate;
    private LocalDate endDate;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private Boolean available;
}
