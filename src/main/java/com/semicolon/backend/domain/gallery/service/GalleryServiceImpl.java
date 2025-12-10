package com.semicolon.backend.domain.gallery.service;

import com.semicolon.backend.domain.gallery.dto.GalleryDTO;
import com.semicolon.backend.domain.gallery.dto.GalleryImageDTO;
import com.semicolon.backend.domain.gallery.entity.Gallery;
import com.semicolon.backend.domain.gallery.entity.GalleryImage;
import com.semicolon.backend.domain.gallery.repository.GalleryRepository;
import com.semicolon.backend.global.file.service.FileUploadService;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService {

    private final GalleryRepository galleryrepository;
    private final FileUploadService fileUploadService;

    @Override
    @Transactional
    public void register(GalleryDTO dto) { //갤러리 신규 등록 시 실행
        Gallery newGallery = Gallery.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .build(); //파일을 제외한 나머지를 설정
        if (dto.getImages() != null && !dto.getImages().isEmpty()) { //이미지 파일 유효성 검사 후
            List<GalleryImage> galleryImages = dto.getImages().stream().map(image -> GalleryImage.builder()
                    .imageUrl(image.getImageUrl()) //갤러리이미지(갤러리의 자식 엔터티) 구현
                    .thumbnailUrl(image.getThumbnailUrl())
                    .build()).toList();
            galleryImages.forEach(image -> newGallery.addImage(image));
            //갤러리 엔터티에 구현한 addImages 실행하여 넣어줌
        }
        galleryrepository.save(newGallery);
    }

    @Override
    public PageResponseDTO<GalleryDTO> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1,pageRequestDTO.getSize()-1
                , Sort.by("createdAt").descending()
        );
        String keyword = pageRequestDTO.getKeyword();
        String type = pageRequestDTO.getType();
        Page<Gallery> result;
        if(keyword==null || keyword.isEmpty()){
            result=galleryrepository.findAll(pageable);
        }else {
            if("t".equals(type)){
                result=galleryrepository.findByTitleContaining(keyword,pageable);
            } else if ("c".equals(type)) {
                result=galleryrepository.findByContentContaining(keyword, pageable);
            }else{
                result=galleryrepository.findByContentContainingOrTitleContaining(keyword,keyword,pageable);
            }
        }
        List<GalleryDTO> dtoList = result.getContent().stream()
                .map(gal -> convertEntityToDTO(gal))
                .toList();
        return PageResponseDTO.<GalleryDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCnt(result.getTotalElements())
                .build();
    }

    @Override
    public GalleryDTO getOne(Long id) {
        Gallery gallery = galleryrepository.findById(id).get();
        return convertEntityToDTO(gallery);
    }

    @Override
    public void increaseViewCount(Long id) {
        Gallery gallery = galleryrepository.findById(id).get();
        gallery.setViewCount(gallery.getViewCount()+1);
        galleryrepository.save(gallery);
    }

    @Override
    @Transactional
    public void update(Long id, GalleryDTO dto) {
        Gallery gallery = galleryrepository.findById(id).orElseThrow();
        gallery.setUpdatedAt(LocalDateTime.now());
        gallery.setTitle(dto.getTitle());
        gallery.setContent(dto.getContent());
        List<String> newUrls = dto.getImages().stream().map(i -> i.getImageUrl()).toList();
        gallery.getImages().removeIf(oldImg -> {
            if (!newUrls.contains(oldImg.getImageUrl())) {
                fileUploadService.deleteFile(oldImg.getImageUrl(), oldImg.getThumbnailUrl());
                return true;
            }
            return false;
        });
        for (GalleryImageDTO newImage : dto.getImages()) {
            boolean exists = gallery.getImages().stream().anyMatch(i -> i.getImageUrl().equals(newImage.getImageUrl()));
            if (!exists) {
                gallery.addImage(convertImageDtoToEntity(newImage));
            }
        }
    }

    @Override
    public void delete(Long id) {
        Gallery gallery = galleryrepository.findById(id).orElseThrow(()->new IllegalArgumentException("삭제할 게시물이 없습니다."));
        for(GalleryImage image : gallery.getImages()){
            fileUploadService.deleteFile(image.getImageUrl(), image.getThumbnailUrl());
        }
        galleryrepository.deleteById(id);
    }

    private GalleryImage convertImageDtoToEntity(GalleryImageDTO dto){
        return GalleryImage.builder()
                .imageUrl(dto.getImageUrl())
                .thumbnailUrl(dto.getThumbnailUrl())
                .build();
    } //수정할때 이미지를 제외한 나머지 내용은 프론트엔드에서 보내준 데이터로 하면 되서 없음
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
