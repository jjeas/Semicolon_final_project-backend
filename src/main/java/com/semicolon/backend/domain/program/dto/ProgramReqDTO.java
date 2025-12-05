package com.semicolon.backend.domain.program.dto;

import com.semicolon.backend.domain.guide.entity.GuideCategory;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramReqDTO {

    private Long pno;
    private String content;
    private String programName;
    private List<String> deletedNo;

    private MultipartFile[] files;
}
