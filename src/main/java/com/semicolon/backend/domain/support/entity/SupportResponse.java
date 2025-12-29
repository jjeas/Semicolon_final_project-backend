package com.semicolon.backend.domain.support.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "tbl_support_response")
@Entity
public class SupportResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="response_id")
    private Long id;

    @Column(name = "content",nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "support_no")
    private Support support;
}
