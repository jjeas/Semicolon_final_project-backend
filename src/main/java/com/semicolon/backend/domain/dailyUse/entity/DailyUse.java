package com.semicolon.backend.domain.dailyUse.entity;

import com.semicolon.backend.domain.facility.entity.Facility;
import com.semicolon.backend.domain.facility.entity.FacilitySpace;
import com.semicolon.backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_daily_use")
@ToString
@Getter
@Setter
@Builder
public class DailyUse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dailyUse_id")
    private Long id;

    @Column(name = "dailyUse_date", nullable = false)
    private LocalDate useDate;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10)
    @Builder.Default
    private DailyUseStatus useStatus = DailyUseStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", nullable = false)
    private FacilitySpace space;




}
