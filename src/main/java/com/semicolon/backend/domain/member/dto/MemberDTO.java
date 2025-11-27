package com.semicolon.backend.domain.member.dto;

import com.semicolon.backend.domain.member.entity.MemberRole;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private long memberId;
    private String memberLoginId;
    private String memberName;
    private String memberPassword;

    private MemberRole memberRole;

    private String memberEmail;
    private String memberAddress;
    private String memberPhoneNumber;
    private String memberGender;

    private LocalDate memberBirthDate;
    private LocalDateTime memberJoinDate;
}
