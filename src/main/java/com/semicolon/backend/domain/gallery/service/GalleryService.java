package com.semicolon.backend.domain.gallery.service;

import com.semicolon.backend.domain.gallery.dto.GalleryDTO;

import java.util.List;

public interface GalleryService {
    public void register(GalleryDTO dto);
    public List<GalleryDTO> getList();
    public GalleryDTO getOne(Long id);
    public void increaseViewCount(Long id);
    void update(Long id, GalleryDTO dto);
    void delete(Long id);
}
