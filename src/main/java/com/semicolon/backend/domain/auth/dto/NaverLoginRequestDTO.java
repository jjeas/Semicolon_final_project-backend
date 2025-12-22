package com.semicolon.backend.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverLoginRequestDTO {
    private String code;
}
