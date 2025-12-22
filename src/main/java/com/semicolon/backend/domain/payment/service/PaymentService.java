package com.semicolon.backend.domain.payment.service;

import com.semicolon.backend.domain.payment.dto.PaymentRequestDTO;
import com.semicolon.backend.domain.payment.dto.PaymentResponseDTO;

public interface PaymentService {
    void verifyAndRegister(PaymentRequestDTO dto, String loginId);
    void cancelPayment(String paymentId, String reason);
}
