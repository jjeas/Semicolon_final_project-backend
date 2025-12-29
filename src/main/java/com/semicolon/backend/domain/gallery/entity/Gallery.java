package com.semicolon.backend.domain.gallery.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_gallery")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gallery_id")
    private Long galleryId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = true, columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // Builder 사용 시 초기화
    private List<GalleryImage> images = new ArrayList<>();

    public void addImage(GalleryImage image) {
        this.images.add(image);
        image.setGallery(this); // 자식에게도 부모(this)를 설정
    }
}
