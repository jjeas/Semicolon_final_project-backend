package com.semicolon.backend.domain.partner.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_partner_file")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    private String originalName;
    private String savedName;
    private String fileType;
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_request_no")
    private Partner partner;


}
