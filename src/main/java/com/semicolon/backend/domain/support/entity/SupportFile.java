package com.semicolon.backend.domain.support.entity;

import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.partner.entity.Partner;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "support")
@Table(name = "tbl_support_file")
@Entity

public class SupportFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long fileNo;

    private String originalName;
    private String savedName;
    private String filePath;

    @Column(name = "support_upload_date", nullable = false)
    private LocalDateTime uploadDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "support_no")
    private Support support;

}
