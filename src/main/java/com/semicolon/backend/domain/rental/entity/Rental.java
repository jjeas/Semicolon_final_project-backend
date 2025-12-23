package com.semicolon.backend.domain.rental.entity;

import com.semicolon.backend.domain.facility.entity.FacilitySpace;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.payment.entity.Payment;
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

        @Column(name = "created_at", nullable = false)
        private LocalDateTime createdAt;

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

        @Column(name = "rental_price", nullable = false)
        private Long price;

        @Column(name = "rental_name", nullable = false)
        private String name;

        @Column(name = "rental_phoneNumber", nullable = false)
        private String phoneNumber;

        @Column(name = "rental_memo")
        private String memo;

        @Enumerated(EnumType.STRING)
        private RentalStatus status;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "payment_id")
        private Payment payment;
}
