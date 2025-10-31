package com.semicolon.backend.domain.notice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int viewCount;
}
