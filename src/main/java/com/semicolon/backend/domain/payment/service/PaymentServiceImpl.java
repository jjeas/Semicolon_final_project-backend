package com.semicolon.backend.domain.payment.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.semicolon.backend.domain.dailyUse.dto.DailyUseDTO;
import com.semicolon.backend.domain.dailyUse.dto.GymDailyUseDTO;
import com.semicolon.backend.domain.dailyUse.entity.DailyUse;
import com.semicolon.backend.domain.dailyUse.entity.GymDailyUse;
import com.semicolon.backend.domain.dailyUse.entity.GymDailyUseStatus;
import com.semicolon.backend.domain.dailyUse.repository.DailyUseRepository;
import com.semicolon.backend.domain.dailyUse.repository.GymDailyUseRepository;
import com.semicolon.backend.domain.facility.entity.FacilitySpace;
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
import com.semicolon.backend.domain.rental.entity.Rental;
import com.semicolon.backend.domain.rental.entity.RentalStatus;
import com.semicolon.backend.domain.rental.repository.RentalRepository;
import com.semicolon.backend.global.reservationFilter.ReservationFilter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final RegistrationRepository registrationRepository;
    private final LessonRepository lessonRepository;
    private final FacilitySpaceRepository facilitySpaceRepository;
    private final DailyUseRepository dailyUseRepository;
    private final GymDailyUseRepository gymDailyUseRepository;
    private final ReservationFilter reservationFilter;
    private final RentalRepository rentalRepository;

    @Value("${iamport.api.key}")
    private String apiKey;

    @Value("${iamport.api.secret}")
    private String apiSecret;

    // V1 API: 액세스 토큰 발급
    private String getAccessToken() {
        RestClient restClient = RestClient.create();
        try {
            Map<String, String> body = new HashMap<>();
            body.put("imp_key", apiKey);
            body.put("imp_secret", apiSecret);

            IamportTokenResponse response = restClient.post()
                    .uri("https://api.iamport.kr/users/getToken")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(IamportTokenResponse.class);

            if (response == null || response.getResponse() == null) {
                throw new RuntimeException("아임포트 토큰 발급 실패: 응답 없음");
            }
            return response.getResponse().getAccessToken();
        } catch (Exception e) {
            log.error("토큰 발급 중 오류: {}", e.getMessage());
            throw new RuntimeException("아임포트 토큰 발급 실패");
        }
    }

    // V1 API: 결제 정보 조회
    public IamportPaymentResponse verifyIamport(String impUid, String accessToken) {
        RestClient restClient = RestClient.create();
        return restClient.get()
                .uri("https://api.iamport.kr/payments/" + impUid)
                .header("Authorization", accessToken) // Bearer 없음
                .retrieve()
                .body(IamportPaymentResponse.class);
    }

    // --- 내부 DTO 클래스들 (V1 응답 구조용) ---
    @Getter @JsonIgnoreProperties(ignoreUnknown = true)
    static class IamportTokenResponse {
        private Response response;
        @Getter @JsonIgnoreProperties(ignoreUnknown = true)
        static class Response {
            @JsonProperty("access_token")
            private String accessToken;
        }
    }

    @Getter @JsonIgnoreProperties(ignoreUnknown = true)
    static class IamportPaymentResponse {
        private Integer code;
        private String message;
        private PaymentData response;

        @Getter @JsonIgnoreProperties(ignoreUnknown = true)
        static class PaymentData {
            private String status; // "paid" (소문자 주의)
            private Long amount;
            @JsonProperty("imp_uid")
            private String impUid;
            @JsonProperty("merchant_uid")
            private String merchantUid;
        }
    }
    // ------------------------------------

    @Transactional
    public void verifyAndRegister(PaymentRequestDTO dto, String loginId) {
        // 1. 아임포트 토큰 발급
        String accessToken = getAccessToken();

        // 2. 결제 내역 조회 (V1)
        IamportPaymentResponse apiResponse = verifyIamport(dto.getPaymentId(), accessToken);

        if (apiResponse == null || apiResponse.getResponse() == null) {
            throw new RuntimeException("결제 정보를 찾을 수 없습니다.");
        }

        IamportPaymentResponse.PaymentData paymentData = apiResponse.getResponse();

        // 3. 검증 로직
        if (paymentRepository.existsByPaymentId(paymentData.getImpUid())) {
            throw new RuntimeException("이미 처리된 결제 건입니다.");
        }
        if (!"paid".equals(paymentData.getStatus())) { // V1은 status가 소문자 "paid"
            throw new RuntimeException("결제가 완료되지 않았습니다. 상태: " + paymentData.getStatus());
        }
        if (dto.getPrice().longValue() != paymentData.getAmount().longValue()) {
            // 가격 불일치 시 취소
            cancelPayment(paymentData.getImpUid(), "결제 금액 위변조 감지");
            throw new RuntimeException("결제 금액 불일치");
        }

        Member member = memberRepository.findByMemberLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("사용자 없음."));

        // 4. 결제 정보 저장
        Payment payment = Payment.builder()
                .paymentId(paymentData.getImpUid()) // imp_uid 저장
                .orderName(generateOrderName(dto))
                .buyerName(member.getMemberName())
                .buyerEmail(member.getMemberEmail())
                .paidAt(LocalDateTime.now())
                .price(paymentData.getAmount())
                .type(Payment.ProductType.valueOf(dto.getProductType()))
                .status(Payment.PaymentStatus.PAID)
                .member(member)
                .build();

        paymentRepository.save(payment);

        // 5. 수강신청 등 로직 실행
        try {
            switch (dto.getProductType()) {
                case "LESSON":
                    saveRegistration(dto, member, payment);
                    break;
                case "RENTAL":
                    saveRental(dto,member,payment);
                    break;
                case "DAILY_USE":
                    saveDailyUse(dto,member,payment);
                    break;
                case "GYM_DAILY_USE":
                    saveGymDailyUse(dto,member,payment);
                    break;
                default:
                    throw new RuntimeException("알 수 없는 상품 타입입니다.:" + dto.getProductType());
            }
        } catch (Exception e) {
            log.error("예약 저장 실패, 결제 취소 진행 imp_uid={}", dto.getPaymentId());
            this.cancelPayment(dto.getPaymentId(), "시스템 에러 " + e.getMessage());
            throw new RuntimeException("예약 실패로 자동 환불 처리: " + e.getMessage());
        }
    }

    @Override
    public void cancelPayment(String impUid, String reason) {
        String accessToken = getAccessToken();
        RestClient restClient = RestClient.create();
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("imp_uid", impUid);
            body.put("reason", reason);

            restClient.post()
                    .uri("https://api.iamport.kr/payments/cancel")
                    .header("Authorization", accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toBodilessEntity();

            log.info("아임포트 결제 취소 성공 {}", impUid);
        } catch (Exception e) {
            log.error("아임포트 결제 취소 실패: {}", impUid, e);
            throw new RuntimeException("환불 요청 중 오류가 발생했습니다.");
        }

        paymentRepository.findByPaymentId(impUid).ifPresent(payment -> {
            payment.setStatus(Payment.PaymentStatus.CANCELLED);
        });
    }

    // --- 아래 헬퍼 메서드들은 기존 로직 유지 ---
    private String generateOrderName(PaymentRequestDTO dto) {
        String type = dto.getProductType();
        Long targetId = dto.getTargetId();
        switch (type) {
            case "RENTAL":
            case "DAILY_USE":
                String spaceName = facilitySpaceRepository.findById(targetId).map(s -> s.getSpaceName()).orElse("시설이용");
                return "[일일이용]" + spaceName; 
            case "GYM_DAILY_USE":
                return "[헬스장 일일이용]";
            case "LESSON":
                String lessonTitle = lessonRepository.findById(targetId).map(l -> l.getTitle()).orElse("수강신청");
                return lessonTitle;
            default:
                return "기타 결제";
        }
    }

    private void saveRegistration(PaymentRequestDTO dto, Member member, Payment payment) {
        Long lessonId = dto.getTargetId();
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new RuntimeException("해당하는 강의가 없습니다."));
        if (lesson.getLessonStatus() != LessonStatus.ACCEPTED) {
            throw new IllegalStateException("현재 모집중인 강의가 아닙니다.");
        }
        if (registrationRepository.existsByMemberAndLessonAndStatus(member, lesson, RegistrationStatus.APPLIED)) {
            throw new IllegalStateException("이미 신청한 강의입니다.");
        }
        Long currentPeople = registrationRepository.countByLesson_IdAndStatus(lessonId, RegistrationStatus.APPLIED);
        if (currentPeople >= lesson.getMaxPeople()) {
            throw new IllegalArgumentException("정원이 가득 찼습니다.");
        }

        Registration registration = Registration.builder()
                .member(member)
                .lesson(lesson)
                .payment(payment)
                .createdAt(LocalDateTime.now())
                .status(RegistrationStatus.APPLIED)
                .build();
        if (currentPeople + 1 >= lesson.getMaxPeople()) lesson.setLessonStatus(LessonStatus.CLOSED);
        registrationRepository.save(registration);
    }

    public void saveDailyUse(PaymentRequestDTO dto, Member member, Payment payment) {
//        public void saveDailyUse(String loginIdFromToken , DailyUseDTO dto) {

        boolean available = reservationFilter.isAvailable(dto.getTargetId(),dto.getStartTime(),dto.getEndTime());
        if (!available) {
            throw new IllegalStateException("해당 시간에 이미 예약이 존재합니다.");
        }

        FacilitySpace facilitySpace = facilitySpaceRepository.findById(dto.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공간입니다."));

        DailyUse  dailyUse= DailyUse.builder()
                .space(facilitySpace)
                .member(member)
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .price(dto.getPrice())
                .createdAt(LocalDateTime.now())
                .payment(payment)
                .build();

        dailyUseRepository.save(dailyUse);

    }

    public void saveGymDailyUse(PaymentRequestDTO dto, Member member, Payment payment) {

        GymDailyUse gymDailyUse = GymDailyUse.builder()
                .date(dto.getDate())
                .createdAt(LocalDateTime.now())
                .price(dto.getPrice())
                .status(GymDailyUseStatus.RESERVED)
                .payment(payment)
                .member(member)
                .build();

        gymDailyUseRepository.save(gymDailyUse);
    }

    public void saveRental(PaymentRequestDTO dto, Member member, Payment payment) {
        boolean available = reservationFilter.isAvailable(dto.getTargetId(),dto.getStartTime(),dto.getEndTime());
        if (!available) {
            throw new IllegalStateException("해당 시간에 이미 예약이 존재합니다.");
        }

        FacilitySpace facilitySpace = facilitySpaceRepository.findById(dto.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공간입니다."));

        Rental rental = Rental.builder()
                .createdAt(LocalDateTime.now())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .member(member)
                .space(facilitySpace)
                .price(dto.getPrice())
                .name(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .memo(dto.getMemo())
                .status(RentalStatus.PENDING)
                .payment(payment)
                .build();

        rentalRepository.save(rental);

    }
}