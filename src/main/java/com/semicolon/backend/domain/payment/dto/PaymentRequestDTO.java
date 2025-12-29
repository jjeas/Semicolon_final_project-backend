package com.semicolon.backend.domain.payment.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {
    private String paymentId;
    private String productType;
    private Long targetId;
    private Long price;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDate date;

    private String name;
    private String phoneNumber;
    private String memo;
}
