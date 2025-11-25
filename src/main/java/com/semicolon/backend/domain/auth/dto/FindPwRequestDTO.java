package com.semicolon.backend.domain.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindPwRequestDTO {
    private String memberName;
    private String memberEmail;
    private String memberLoginId;
}
