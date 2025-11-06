package com.semicolon.backend.domain.notice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_notice")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Notice {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_notice")
    @SequenceGenerator(name = "seq_notice", sequenceName = "SEQ_NOTICE", allocationSize = 1)
    @Column(name = "notice_id")
    private Long noticeId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

//    @Column(name = "content",nullable = false, columnDefinition = "TEXT")
    @Column(name = "content",nullable = false, columnDefinition = "CLOB")
    private String content;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "view_count",nullable = false)
    @Builder.Default
    private int viewCount=0;

}
