package com.semicolon.backend.domain.registration.entity;

import com.semicolon.backend.domain.lesson.entity.Lesson;
import com.semicolon.backend.domain.lesson.entity.LessonStatus;
import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.payment.entity.Payment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_registration")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    private Long registrationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; //누가 신청했는가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson; //어떤 강의를 신청했는가

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; //언제 신청했는가

    @Column(name = "status")
    private RegistrationStatus status; // 신청완료, 신청취소, 대기중

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Enumerated(EnumType.STRING)
    public void cancel(){ //신청 취소 시
        this.status=RegistrationStatus.CANCELED;
        if(this.lesson.getLessonStatus()==LessonStatus.CLOSED)
            this.lesson.setLessonStatus(LessonStatus.ACCEPTED);
    }

}
