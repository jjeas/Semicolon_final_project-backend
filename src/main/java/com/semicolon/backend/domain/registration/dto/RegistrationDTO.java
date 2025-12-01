package com.semicolon.backend.domain.registration.dto;

import com.semicolon.backend.domain.registration.entity.Registration;
import com.semicolon.backend.domain.registration.entity.RegistrationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {
    private Long registrationId;
    private Long lessonId;
    private String lessonTitle;
    private String teacherName;
    private LocalDateTime createdAt;
    private RegistrationStatus status;

    public static RegistrationDTO toDto(Registration registration) {
        return RegistrationDTO.builder()
                .registrationId(registration.getRegistrationId())
                .lessonId(registration.getLesson().getId()) // 객체에서 ID 꺼내기
                .lessonTitle(registration.getLesson().getTitle()) // 객체에서 제목 꺼내기
                .teacherName(registration.getLesson().getPartnerId().getMemberName()) // 선생님 이름까지 타고 들어가서 꺼내기
                .status(registration.getStatus())
                .createdAt(registration.getCreatedAt())
                .build();
    }
}
