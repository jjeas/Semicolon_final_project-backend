package com.semicolon.backend.domain.payment.dto;

import lombok.*;

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
}
