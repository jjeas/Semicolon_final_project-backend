package com.semicolon.backend.domain.faq.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_faq")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Faq {
    @Id
    @Column(name = "faq_id",nullable = false)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_faq")
    @SequenceGenerator(name = "seq_faq", sequenceName = "SEQ_FAQ", allocationSize = 1)
    private Long faqId;

    @Column(name ="question",nullable = false)
    private String question;
    @Column(name = "answer", nullable = false)
    private String answer;
//    @Column(name = "category",nullable = false)
//    private String category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faq_category_id",nullable = false)
    private FaqCategory faqCategory;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at",nullable = true)
    private LocalDateTime updatedAt;
}
