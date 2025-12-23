package com.semicolon.backend.domain.registration.service;

import com.semicolon.backend.domain.lesson.entity.Lesson;
import com.semicolon.backend.domain.lesson.entity.LessonStatus;
import com.semicolon.backend.domain.lesson.repository.LessonRepository;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.member.repository.MemberRepository;
import com.semicolon.backend.domain.payment.entity.Payment;
import com.semicolon.backend.domain.payment.service.PaymentService;
import com.semicolon.backend.domain.registration.dto.RegistrationDTO;
import com.semicolon.backend.domain.registration.entity.Registration;
import com.semicolon.backend.domain.registration.entity.RegistrationStatus;
import com.semicolon.backend.domain.registration.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class RegistrationServiceImpl implements RegistrationService{
    private final MemberRepository memberRepository;
    private final LessonRepository lessonRepository;
    private final RegistrationRepository registrationRepository;
    private final PaymentService paymentService;

    @Override
    @Transactional
    public void register(Long lessonId, String loginId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(()->new IllegalArgumentException("해당하는 강의가 없습니다."));
        Member member = memberRepository.findByMemberLoginId(loginId).orElseThrow(()->new IllegalArgumentException("해당하는 유저가 없습니다."));
        Long currentPeople = registrationRepository.countByLesson_IdAndStatus(lessonId, RegistrationStatus.APPLIED);
        if(lesson.getLessonStatus()!= LessonStatus.ACCEPTED){
            throw new IllegalStateException("현재 모집중인 강의가 아닙니다.");
        }
        if(registrationRepository.existsByMemberAndLessonAndStatus(member, lesson, RegistrationStatus.APPLIED)){
            throw new IllegalStateException("이미 신청한 강의입니다.");
        }
        Registration registration=Registration.builder()
                .lesson(lesson)
                .member(member)
                .createdAt(LocalDateTime.now())
                .status(RegistrationStatus.APPLIED)
                .build();
        if(currentPeople>=lesson.getMaxPeople()) throw new IllegalArgumentException("정원이 가득 찼습니다.");
        if(currentPeople+1>=lesson.getMaxPeople()) registration.getLesson().setLessonStatus(LessonStatus.CLOSED);
        registrationRepository.save(registration);
    }

    @Transactional
    @Override
    public void cancel(String loginId, Long registrationId) {
        Registration registration = registrationRepository.findById(registrationId).orElseThrow(()->new IllegalArgumentException("해당하는 수강신청이 없습니다."));
        Member member = memberRepository.findByMemberLoginId(loginId).orElseThrow(()->new IllegalArgumentException("해당하는 유저가 없습니다."));
        if(!registration.getMember().getMemberLoginId().equals(member.getMemberLoginId())){
            throw new IllegalStateException("본인 강의만 취소 가능합니다.");
        }
        if(registration.getStatus() ==RegistrationStatus.CANCELED){
            throw new IllegalStateException("이미 취소된 강의입니다.");
        }
        Payment payment = registration.getPayment();
        if(payment!=null){
            paymentService.cancelPayment(payment.getPaymentId(), "회원 요청에 의한 수강 취소");
        }
        registration.cancel();
    }

    @Override
    public List<RegistrationDTO> getList(String loginId) {
        Member member = memberRepository.findByMemberLoginId(loginId).orElseThrow(()->new IllegalArgumentException("해당하는 유저가 없습니다."));
        List<Registration> regList = registrationRepository.findByMemberId(member.getMemberId());
        if(regList==null || regList.isEmpty()){
            return Collections.emptyList();
        }
        return regList.stream().map(i-> RegistrationDTO.toDto(i)).toList();
    }

    @Override
    public boolean checkRegistrationStatus(String loginId, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(()->new IllegalArgumentException("해당하는 강의가 없습니다."));
        Member member = memberRepository.findByMemberLoginId(loginId).orElseThrow(()->new IllegalArgumentException("해당하는 유저가 없습니다."));
        return registrationRepository.existsByMemberAndLessonAndStatus(member, lesson, RegistrationStatus.APPLIED);
    }
}
