package com.semicolon.backend.domain.gallery.entity;

import com.semicolon.backend.domain.gallery.dto.GalleryDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

@Entity
@Table(name = "tbl_gallery_image")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class GalleryImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(name ="image_url", nullable = false)
    private String imageUrl;

    @Column(name ="thumbnail_url", nullable = false)
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gallery_id", nullable = false)
    private Gallery gallery;

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }
}
