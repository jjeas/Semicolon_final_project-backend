package com.semicolon.backend.domain.dailyUse.entity;

import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.payment.entity.Payment;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_gym_daily_use")
@ToString
@Getter
@Setter
@Builder
public class GymDailyUse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gym_id")
    private Long id;

    @Column(name = "gym_date")
    private LocalDate date;

    @Column(name = "price", nullable = false)
    private long price;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GymDailyUseStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

}
