package com.semicolon.backend.domain.gallery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_gallery")
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_gallery", sequenceName = "SEQ_GALLERY",allocationSize = 1)
    @Column(name = "gallery_id")
    private Long galleryId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = true, columnDefinition = "CLOB")
    private String content;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

}
