package com.semicolon.backend.domain.payment.entity;

import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.registration.entity.Registration;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String paymentId;

    @Column(nullable = false)
    private String orderName;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false)
    private Long price;

    private LocalDateTime paidAt;
    private String buyerName;
    private String buyerEmail;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type")
    private ProductType type;

    public enum ProductType {
        RENTAL, DAILY_USE, GYM_DAILY_USE, LESSON
    }

    public enum PaymentStatus {
        PAID, FAILED, CANCELLED
    }

    public void changeStatus(PaymentStatus status) {
        this.status = status;
    }
}
