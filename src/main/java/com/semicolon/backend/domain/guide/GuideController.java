package com.semicolon.backend.domain.guide;

import com.semicolon.backend.domain.guide.dto.GuideDTO;
import com.semicolon.backend.domain.guide.service.GuideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guide")
@RequiredArgsConstructor
public class GuideController {

    private final GuideService service;

    @PostMapping("/save")
    public ResponseEntity<?> save (@RequestBody GuideDTO dto) {
        service.save(dto);
        return ResponseEntity.ok("guide save 완료");
    }

    @GetMapping("/{category}")
    public ResponseEntity<?> get (@PathVariable String category){
        return ResponseEntity.ok(service.get(category));
    }
}
