package com.semicolon.backend.domain.partner.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Setter
@Getter
public class PartnerFileDTO {
    private Long fileId;
    private String fileCategory;
    private String originalName;
    private String savedName;
    private String filePath;
    private String thumbnailPath;
}
