package com.semicolon.backend.domain.rental.entity;

import com.semicolon.backend.domain.facility.entity.FacilitySpace;
import com.semicolon.backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_rental")
@ToString(exclude = { "space"})
@Getter
@Setter
@Builder
public class Rental {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "rental_id")
        private Long id;

        @Column(name = "start_time", nullable = false)
        private LocalDateTime startTime;

        @Column(name = "end_time", nullable = false)
        private LocalDateTime endTime;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "member_id", nullable = false)
        private Member member;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "space_id", nullable = false)
        private FacilitySpace space;

}
