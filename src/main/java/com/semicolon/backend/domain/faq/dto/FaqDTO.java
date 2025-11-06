package com.semicolon.backend.domain.faq.dto;

import com.semicolon.backend.domain.faq.entity.FaqCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FaqDTO {
    private Long faqId;
    private String question;
    private String answer;
    private Long faqCategoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
