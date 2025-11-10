package com.semicolon.backend.domain.gallery.service;

import com.semicolon.backend.domain.gallery.dto.GalleryDTO;
import com.semicolon.backend.domain.gallery.dto.GalleryImageDTO;
import com.semicolon.backend.domain.gallery.entity.Gallery;
import com.semicolon.backend.domain.gallery.entity.GalleryImage;
import com.semicolon.backend.domain.gallery.repository.GalleryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService {

    private final GalleryRepository Galleryrepository;
    private final GalleryRepository GalleryImagerepository;

    @Override
    @Transactional
    public void register(GalleryDTO dto) {
        Gallery newGallery = Gallery.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .build();
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            List<GalleryImage> galleryImages = dto.getImages().stream().map(image -> GalleryImage.builder()
                    .imageUrl(image.getImageUrl())
                    .thumbnailUrl(image.getThumbnailUrl())
                    .build()).toList();
            galleryImages.forEach(image -> newGallery.addImage(image));
        }
        Galleryrepository.save(newGallery);
    }

    @Override
    public List<GalleryDTO> getList() {
        return Galleryrepository.findAll().stream().map(gal ->
                convertEntityToDTO(gal)
        ).toList();
    }

    @Override
    public GalleryDTO getOne(Long id) {
        Gallery gallery = Galleryrepository.findById(id).get();
        return convertEntityToDTO(gallery);
    }

    private GalleryDTO convertEntityToDTO(Gallery gallery) {

        // 1. 자식(GalleryImage) 리스트를 ImageInfoDTO 리스트로 변환
        List<GalleryImageDTO> imageInfos = gallery.getImages().stream()
                .map(image -> {
                    GalleryImageDTO info = new GalleryImageDTO();
                    // (Lombok이 없어서 수동 set. Builder나 생성자 사용 권장)
                    // 이 예제에서는 ImageInfoDTO에 @NoArgsConstructor와 @Setter가 있다고 가정
                    // 혹은 Builder 패턴 사용
                    return GalleryImageDTO.builder()
                            .imageUrl(image.getImageUrl())
                            .thumbnailUrl(image.getThumbnailUrl())
                            .build();
                }).toList();
        return GalleryDTO.builder()
                .galleryId(gallery.getGalleryId())
                .title(gallery.getTitle())
                .content(gallery.getContent())
                .viewCount(gallery.getViewCount())
                .createdAt(gallery.getCreatedAt())
                .updatedAt(gallery.getUpdatedAt())
                .images(imageInfos) // 조립된 이미지 리스트
                .build();
    }
}
