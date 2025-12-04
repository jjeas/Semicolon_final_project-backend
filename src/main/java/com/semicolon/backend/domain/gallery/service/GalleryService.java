package com.semicolon.backend.domain.gallery.service;

import com.semicolon.backend.domain.gallery.dto.GalleryDTO;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;

import java.util.List;

public interface GalleryService {
    public void register(GalleryDTO dto);
    public PageResponseDTO<GalleryDTO> getList(PageRequestDTO pageRequestDTO);
    public GalleryDTO getOne(Long id);
    public void increaseViewCount(Long id);
    void update(Long id, GalleryDTO dto);
    void delete(Long id);
}
