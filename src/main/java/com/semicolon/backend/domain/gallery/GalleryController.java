package com.semicolon.backend.domain.gallery;

import com.semicolon.backend.domain.gallery.dto.GalleryDTO;
import com.semicolon.backend.domain.gallery.service.GalleryService;
import com.semicolon.backend.global.file.service.FileUploadService;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/community/gallery")
@RequiredArgsConstructor
public class GalleryController {
    private final GalleryService galleryService;

    @GetMapping("")
    public ResponseEntity<PageResponseDTO<GalleryDTO>> getList(PageRequestDTO dto){
        log.info("gallery 전체 조회 컨트롤러 실행");
        return ResponseEntity.ok(galleryService.getList(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GalleryDTO> getOne(@PathVariable("id") Long id){
        log.info("gallery read 컨트롤러 실행");
        return ResponseEntity.ok(galleryService.getOne(id));
    }

    @PostMapping("/admin")
    public ResponseEntity<String> register(@RequestBody GalleryDTO dto){
        log.info("gallery 등록 컨트롤러 실행");
        galleryService.register(dto);
        return ResponseEntity.ok("gallery 등록 성공");
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<String> addViewCount(@PathVariable long id){
        galleryService.increaseViewCount(id);
        return ResponseEntity.ok("조회수 1 증가 완료");
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody GalleryDTO dto){
        galleryService.update(id, dto);
        return ResponseEntity.ok("갤러리 수정 완료");
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        galleryService.delete(id);
        return ResponseEntity.ok("갤러리 삭제 완료");
    }

}
