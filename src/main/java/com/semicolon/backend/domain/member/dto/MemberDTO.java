package com.semicolon.backend.domain.member.dto;

import com.semicolon.backend.domain.member.entity.MemberRole;
import lombok.*;

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
    private String memberPassword;
    private String memberName;

    private String memberRole;

    private String memberEmail;
    private String memberAddress;
    private String memberPhoneNumber;
    private String memberGender;

    private LocalDateTime memberBirthDate;
    private LocalDateTime memberJoinDate;
}
