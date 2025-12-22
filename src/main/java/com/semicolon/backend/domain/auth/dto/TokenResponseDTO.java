package com.semicolon.backend.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenResponseDTO {
    private String accessToken;
    private String memberRole;
    private String loginId;
}
