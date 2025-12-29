package com.semicolon.backend.domain.schedule.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_schedule")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "content", nullable = false)
    private String content; // 세부 시간 표현은 이걸로 하면 될듯
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
}
