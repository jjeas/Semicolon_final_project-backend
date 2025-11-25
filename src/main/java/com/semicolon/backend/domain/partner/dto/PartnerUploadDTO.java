package com.semicolon.backend.domain.partner.dto;

import com.semicolon.backend.domain.member.dto.MemberDTO;
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
public class PartnerUploadDTO {
    private long requestNo;
    private MemberDTO member;
    private LocalDateTime requestDate;
    private String status;
    private List<String> partnerClass;

    private List<PartnerFileDTO> resumeFiles;
    private List<PartnerFileDTO> certFiles;
    private List<PartnerFileDTO> bankFiles;

}
