package com.semicolon.backend.domain.guide.dto;

import com.semicolon.backend.domain.guide.entity.GuideCategory;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuideReqDTO {
    private GuideCategory category;
    private String html;
    private List<String> deletedNo;

    private MultipartFile[] files;
}
