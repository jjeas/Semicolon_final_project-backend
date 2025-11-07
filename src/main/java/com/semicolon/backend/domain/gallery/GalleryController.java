package com.semicolon.backend.domain.gallery;

import com.semicolon.backend.domain.gallery.dto.GalleryDTO;
import com.semicolon.backend.domain.gallery.service.GalleryService;
import com.semicolon.backend.global.file.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/gallery")
@RequiredArgsConstructor
public class GalleryController {
    private final GalleryService galleryService;
    private final FileUploadService fileUploadService;

    @GetMapping("")
    public ResponseEntity<List<GalleryDTO>> getList(){
        log.info("gallery 전체 조회 컨트롤러 실행");
        return ResponseEntity.ok(galleryService.getList());
    }

    @PostMapping("")
    public ResponseEntity<String> register(@RequestBody GalleryDTO dto){
        log.info("gallery 등록 컨트롤러 실행");
        galleryService.register(dto);
        return ResponseEntity.ok("gallery 등록 성공");
    }

}
