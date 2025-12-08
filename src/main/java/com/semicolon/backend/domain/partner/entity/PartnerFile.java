package com.semicolon.backend.domain.partner.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_partner_file")
@ToString(exclude = "partner")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;
    private String fileCategory;
    private String originalName;
    private String savedName;
    private String filePath;
    private String thumbnailPath;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_request_no")
    private Partner partner;


}
