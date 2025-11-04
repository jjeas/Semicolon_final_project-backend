package com.semicolon.backend.domain.program.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProgramDTO {
    private long pno;
    private String content;
    private String programName;
}