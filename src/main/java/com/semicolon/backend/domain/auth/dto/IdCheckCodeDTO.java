package com.semicolon.backend.domain.auth.dto;

import lombok.Getter;

@Getter
public class IdCheckCodeDTO {
    private String memberName;
    private String memberEmail;
    private String authCode;
}
