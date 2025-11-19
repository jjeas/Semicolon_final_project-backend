package com.semicolon.backend.domain.guide.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GuideDTO {
    private Long id;
    private String category;
    private LocalDateTime updatedDate;
    private String html;
}
