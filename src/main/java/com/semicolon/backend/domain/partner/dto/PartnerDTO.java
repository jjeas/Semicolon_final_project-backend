package com.semicolon.backend.domain.partner.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerDTO {
    private long requestNo;

    private long memberId;
    private LocalDateTime requestDate;
    private List<String> requestStatus;

    @Builder.Default
    private List<MultipartFile> partnerClass = new ArrayList<>();

    @Builder.Default
    private List<MultipartFile> partnerFiles = new ArrayList<>();

    @Builder.Default
    private List<String> uploadPartnerFileName = new ArrayList<>();
}
