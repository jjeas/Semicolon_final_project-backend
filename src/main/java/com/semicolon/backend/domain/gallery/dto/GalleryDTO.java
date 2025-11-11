package com.semicolon.backend.domain.gallery.dto;

import com.semicolon.backend.domain.gallery.entity.Gallery;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GalleryDTO {
    private Long galleryId;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<GalleryImageDTO> images;
}
