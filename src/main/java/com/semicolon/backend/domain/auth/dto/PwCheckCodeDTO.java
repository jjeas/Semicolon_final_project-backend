package com.semicolon.backend.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PwCheckCodeDTO {
    private String memberName;
    private String memberEmail;;
    private String memberLoginId;
    private String authCode;
}
