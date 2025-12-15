package com.semicolon.backend.domain.auth.dto;

import com.semicolon.backend.domain.member.entity.MemberRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {
    private String accessToken;
    private Long memberId;
    private String loginId;
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;
    //로그인 성공 후 반환할 토큰 아이디 권한
}
