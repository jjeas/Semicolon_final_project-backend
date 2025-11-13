package com.semicolon.backend.domain.notice.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class NoticeFileDTO {

    private Long id;
    private String originalName;
    private String savedName;
    private String filePath;
    private String thumbnailPath;

}
