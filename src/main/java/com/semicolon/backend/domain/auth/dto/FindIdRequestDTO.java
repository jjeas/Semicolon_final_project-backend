package com.semicolon.backend.domain.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class FindIdRequestDTO {
    private String memberName;
    private String memberEmail;
}
