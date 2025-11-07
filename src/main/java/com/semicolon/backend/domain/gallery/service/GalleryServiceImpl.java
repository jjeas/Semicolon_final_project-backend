package com.semicolon.backend.domain.gallery.service;

import com.semicolon.backend.domain.gallery.dto.GalleryDTO;
import com.semicolon.backend.domain.gallery.entity.Gallery;
import com.semicolon.backend.domain.gallery.repository.GalleryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService{

    private final GalleryRepository repository;

    @Override
    public void register(GalleryDTO dto) {
        Gallery newGallery = new Gallery(
                null, //시퀀스가 자동으로 id 생성
                dto.getTitle(),
                dto.getContent(),
                0, //기본값인 0으로
                LocalDateTime.now(),
                null, //업데이트 되면 넣어야되니 비워둠
                dto.getThumbnailUrl(),
                dto.getImageUrl()
                );
        repository.save(newGallery);
    }

    @Override
    public List<GalleryDTO> getList() {
        return repository.findAll().stream().map(gal-> new GalleryDTO(
                gal.getGalleryId(),
                gal.getTitle(),
                gal.getContent(),
                gal.getViewCount(),
                gal.getCreatedAt(),
                gal.getUpdatedAt(),
                gal.getThumbnailUrl(),
                gal.getImageUrl()
        )).toList();
    }
}
