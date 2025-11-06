package com.semicolon.backend.domain.faq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class FaqCategoryDTO {
    private Long faqCategoryId;
    private String categoryName;
}
