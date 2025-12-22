package com.semicolon.backend.domain.payment;

import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.payment.dto.PaymentRequestDTO;
import com.semicolon.backend.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService service;

    @PostMapping("/complete")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentRequestDTO dto, @AuthenticationPrincipal String loginIdFromToken){
        if (loginIdFromToken==null){
            return ResponseEntity.status(401).body("인증 정보가 없음");
        }
        try {
            service.verifyAndRegister(dto,loginIdFromToken);
            return ResponseEntity.ok("결제 및 예약 완료");
        } catch(Exception e){
            log.error("결제실패 {}",e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
