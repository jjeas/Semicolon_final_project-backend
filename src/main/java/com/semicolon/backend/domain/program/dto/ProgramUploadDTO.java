package com.semicolon.backend.domain.program.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProgramUploadDTO {
    private long fileNo;

    private String fileName;
    private String filePath;
    private String savedName;
}
