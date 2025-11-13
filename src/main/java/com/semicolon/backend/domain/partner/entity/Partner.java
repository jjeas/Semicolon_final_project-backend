package com.semicolon.backend.domain.partner.entity;

import com.semicolon.backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_partner")
@ToString(exclude = "member")

public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partner_request_no")
    private long requestNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    private PartnerStatus status;

    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tbl_partner_class", joinColumns = @JoinColumn(name = "partner_request_no"))
    @Column(name = "class_name")
    private List<String> partnerClass = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartnerFile> files = new ArrayList<>();

    public void addPartner(PartnerFile partnerFile) {
        files.add(partnerFile);
        partnerFile.setPartner(this);
    }

}
