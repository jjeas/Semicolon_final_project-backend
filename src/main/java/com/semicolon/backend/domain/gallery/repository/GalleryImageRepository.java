package com.semicolon.backend.domain.gallery.repository;

import com.semicolon.backend.domain.gallery.entity.Gallery;
import com.semicolon.backend.domain.gallery.entity.GalleryImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryImageRepository extends JpaRepository<GalleryImage,Long> {
}
