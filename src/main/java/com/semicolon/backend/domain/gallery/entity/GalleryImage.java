package com.semicolon.backend.domain.gallery.entity;

import com.semicolon.backend.domain.gallery.dto.GalleryDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class GalleryImage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long imageId;

    @Column(name ="image_url", nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gallery_id")
    private Gallery gallery;
}
