package com.semicolon.backend.domain.guide.dto;

import com.semicolon.backend.domain.guide.entity.GuideCategory;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "html")
public class GuideDTO {

    private Long id;
    private GuideCategory category;
    private String html;
    private LocalDateTime updatedDate;

    private List<GuideUploadDTO> uploadFiles;
}
