package com.semicolon.backend.domain.support.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.semicolon.backend.domain.member.dto.MemberDTO;
import com.semicolon.backend.domain.support.entity.SupportResponse;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SupportUploadDTO {
    private long supportNo;
    private MemberDTO member;

    private String supportTitle;
    private String supportContent;
    private String status;
    private List<String> fileName;
    private List<String> filePath;
    private List<String> savedName;

    private List<SupportResponseDTO> response;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;
}
