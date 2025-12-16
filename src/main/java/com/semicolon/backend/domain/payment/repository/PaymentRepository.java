package com.semicolon.backend.domain.payment.repository;

import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentId(String paymentId);
    boolean existsByPaymentId(String paymentId);
    List<Payment> findAllByMemberOrderByPaidAtDesc(Member member);
}
