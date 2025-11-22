package com.semicolon.backend.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResetPasswordDTO {
    private String memberName;
    private String memberEmail;
    private String memberLoginId;
    private String authCode;
    private String newPassword;
}
