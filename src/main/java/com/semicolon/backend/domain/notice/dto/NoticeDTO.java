package com.semicolon.backend.domain.notice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NoticeDTO {
    private Long noticeId;
    private String title;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    private int viewCount;

    private MultipartFile[] files; // formdata 로 프런트에서 보내는 용도

    private List<NoticeFileDTO> fileList; // 백엔드에서 프런트로 보내는 용도
}
