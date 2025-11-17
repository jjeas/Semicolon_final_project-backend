package com.semicolon.backend.domain.support.entity;

import com.semicolon.backend.domain.member.entity.Member;
import com.semicolon.backend.domain.partner.entity.PartnerStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

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
    private SupportStatus status;

    @Column(name = "support_title", nullable = false)
    private String title;

    @Lob
    @Column(name = "support_content", nullable = false)
    private String content;
}