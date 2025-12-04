package com.semicolon.backend.domain.gallery.repository;

import com.semicolon.backend.domain.gallery.entity.Gallery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery,Long> {
    Page<Gallery> findByContentContaining(String keyword, Pageable pageable);
    Page<Gallery> findByTitleContaining(String keyword, Pageable pageable);
    Page<Gallery> findByContentContainingOrTitleContaining(String content, String title, Pageable pageable);
}
