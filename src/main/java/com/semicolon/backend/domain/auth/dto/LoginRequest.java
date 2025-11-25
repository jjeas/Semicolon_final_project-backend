package com.semicolon.backend.domain.auth.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String loginId;
    private String password;
    //로그인 요청 시 보내주는 아이디와 패스워드
}
