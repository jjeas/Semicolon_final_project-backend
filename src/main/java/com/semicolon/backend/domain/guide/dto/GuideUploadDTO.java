package com.semicolon.backend.domain.guide.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GuideUploadDTO {
    private long fileNo;

    private String fileName;
    private String filePath;
    private String savedName;

}
