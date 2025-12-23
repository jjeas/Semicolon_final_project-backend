package com.semicolon.backend.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailCheckDTO {
    private String memberEmail;
    private String authCode;
}
