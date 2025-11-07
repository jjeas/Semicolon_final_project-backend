package com.semicolon.backend.domain.gallery.repository;

import com.semicolon.backend.domain.gallery.entity.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery,Long> {
}
