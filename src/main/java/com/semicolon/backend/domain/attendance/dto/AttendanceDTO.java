package com.semicolon.backend.domain.attendance.dto;

import com.semicolon.backend.domain.attendance.entity.AttendanceStatus;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AttendanceDTO {
    private Long lessonId;
    private Long studentNo;
    private Long attendanceId;
    private String name;
    private LocalDate attendanceDate;
    private AttendanceStatus status;
    private String memo;
}
