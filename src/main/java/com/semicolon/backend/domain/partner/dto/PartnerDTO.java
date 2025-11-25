package com.semicolon.backend.domain.partner.dto;

import com.semicolon.backend.domain.partner.entity.PartnerStatus;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PartnerDTO {
    private long requestNo;
    private LocalDateTime requestDate;
    private String status;
    private List<String> partnerClass;

    private MultipartFile[] resumeFiles;
    private MultipartFile[] certFiles;
    private MultipartFile[] bankFiles;

    public PartnerDTO (PartnerStatus status){
        this.status = (status != null) ? status.name() : "";
    }
}
