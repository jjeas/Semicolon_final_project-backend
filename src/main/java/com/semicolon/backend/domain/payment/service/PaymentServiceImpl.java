package com.semicolon.backend.domain.payment.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.semicolon.backend.domain.dailyUse.repository.DailyUseRepository;
import com.semicolon.backend.domain.facility.repository.FacilitySpaceRepository;
import com.semicolon.backend.domain.lesson.entity.Lesson;
import com.semicolon.backend.domain.lesson.entity.LessonStatus;
import com.semicolon.backend.domain.lesson.repository.LessonRepository;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.payment.dto.PaymentRequestDTO;
import com.semicolon.backend.domain.payment.entity.Payment;
import com.semicolon.backend.domain.payment.repository.PaymentRepository;
import com.semicolon.backend.domain.registration.entity.Registration;
import com.semicolon.backend.domain.registration.entity.RegistrationStatus;
import com.semicolon.backend.domain.registration.repository.RegistrationRepository;
import com.semicolon.backend.domain.rental.repository.RentalRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;

    private final RentalRepository rentalRepository;
    private final RegistrationRepository registrationRepository;
    private final DailyUseRepository dailyUseRepository;
    private final LessonRepository lessonRepository;

    private final FacilitySpaceRepository facilitySpaceRepository;

    @Value("${PORTONE_API_SECRET}")
    private String apiSecret;

    @Override
    public PaymentResponse verifyPortOne(String paymentId) {
        RestClient restClient = RestClient.create();
        return restClient.get()
                .uri("https://api.portone.io/payments/"+paymentId)
                .header("Authorization","PortOne "+apiSecret)
                .retrieve()
                .onStatus((HttpStatusCode status) -> status.value()!=200, (req,res)->{
                    throw new RuntimeException("포트원 결제 조회 실패");
                })
                .body(PaymentResponse.class);
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class PaymentResponse {
        private String status; // "PAID" 등
        private Amount amount; // 중첩된 JSON 구조 처리

        @Getter @JsonIgnoreProperties(ignoreUnknown = true)
        static class Amount {
            private Long total;
        }
    }

    @Transactional
    public void verifyAndRegister(PaymentRequestDTO dto, String loginId){
        if(paymentRepository.existsByPaymentId(dto.getPaymentId())){
            throw new RuntimeException("이미 결제된 내역입니다.");
        }
        PaymentResponse portOneData = verifyPortOne(dto.getPaymentId());
        long realAmount = portOneData.getAmount().getTotal();
        if(!"PAID".equals(portOneData.getStatus())){
            throw new RuntimeException("결제가 완료되지 않았습니다.");
        }
        if(dto.getPrice()!=realAmount){
            throw new RuntimeException("결제금액 불일치");
        }
        Member member = memberRepository.findByMemberLoginId(loginId).orElseThrow(()->new RuntimeException("사용자 없음."));

        Payment payment = Payment.builder()
                .paymentId(dto.getPaymentId())
                .orderName(generateOrderName(dto))
                .buyerName(member.getMemberName())
                .buyerEmail(member.getMemberEmail())
                .paidAt(LocalDateTime.now())
                .price(realAmount)
                .type(Payment.ProductType.valueOf(dto.getProductType()))
                .status(Payment.PaymentStatus.PAID)
                .member(member)
                .build();

        paymentRepository.save(payment);
        switch (dto.getProductType()){
            case "LESSON" :
                saveRegistration(dto,member,payment);
                break;
            default:
                throw new RuntimeException("알 수 없는 상품 타입입니다.:"+dto.getProductType());
        }

    }

    private String generateOrderName(PaymentRequestDTO dto){
        String type = dto.getProductType();
        Long targetId = dto.getTargetId();
        switch(type){
            case "RENTAL" :
            case "DAILY_USE" :
                String spaceName = facilitySpaceRepository.findById(targetId).map(space->space.getSpaceName()).orElse("시설이용");
                return (type.equals("RENTAL")?"[대관]":"[일일이용]") + spaceName;
            case "LESSON" :
                String lessonTitle = lessonRepository.findById(targetId).map(lesson->lesson.getTitle()).orElse("수강신청");
                return lessonTitle;
            default :
                return "기타 결제";
        }
    }

    private void saveRegistration(PaymentRequestDTO dto, Member member, Payment payment){
        Long lessonId = dto.getTargetId();
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(()->new RuntimeException("해당하는 강의가 없습니다."));
        if(lesson.getLessonStatus()!= LessonStatus.ACCEPTED){
            throw new IllegalStateException("현재 모집중인 강의가 아닙니다.");
        }
        if(registrationRepository.existsByMemberAndLessonAndStatus(member, lesson, RegistrationStatus.APPLIED)){
            throw new IllegalStateException("이미 신청한 강의입니다.");
        }
        Long currentPeople = registrationRepository.countByLesson_IdAndStatus(lessonId, RegistrationStatus.APPLIED);
        if(currentPeople>=lesson.getMaxPeople()){
            throw new IllegalArgumentException("정원이 가득 찼습니다.");
        }

        Registration registration = Registration.builder()
                .member(member)
                .lesson(lesson)
                .payment(payment)
                .createdAt(LocalDateTime.now())
                .status(RegistrationStatus.APPLIED)
                .build();
        if(currentPeople+1>=lesson.getMaxPeople()) lesson.setLessonStatus(LessonStatus.CLOSED);
        registrationRepository.save(registration);
    }
}
