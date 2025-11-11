package com.semicolon.backend.domain.gallery.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GalleryImageDTO {
    private String imageUrl;
    private String thumbnailUrl;
}
