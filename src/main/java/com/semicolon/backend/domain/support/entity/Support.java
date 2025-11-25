package com.semicolon.backend.domain.support.entity;

import com.semicolon.backend.domain.member.entity.Member;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "member")
@Table(name = "tbl_support")
@Entity

public class Support {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "support_no")
    private long supportNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "support_create_date", nullable = false)
    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10)
    @Builder.Default
    private SupportStatus status = SupportStatus.WAITING;

    @Column(name = "support_title", nullable = false)
    private String supportTitle;

    @Lob
    @Column(name = "support_content", nullable = false)
    private String supportContent;

    @Builder.Default
    @OneToMany(mappedBy = "support", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupportFile> files = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "support", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupportResponse> response = new ArrayList<>();

    public void addSupport(SupportFile supportFile){
        files.add(supportFile);
        supportFile.setSupport(this);
    }

    public void addResponse(SupportResponse supportResponse){
        response.add(supportResponse);
        supportResponse.setSupport(this);
    }

}