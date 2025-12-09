package com.semicolon.backend.domain.dailyUse;

import com.semicolon.backend.domain.dailyUse.dto.DailyUseDTO;
import com.semicolon.backend.domain.dailyUse.dto.GymDailyUseDTO;
import com.semicolon.backend.domain.dailyUse.service.GymDailyUseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/gymDailyUse")
@RequiredArgsConstructor
public class GymDailyUseController {

    private final GymDailyUseService service;

    @PostMapping("")
    public ResponseEntity<String> register(@AuthenticationPrincipal String loginIdFromToken, @RequestBody GymDailyUseDTO dto){
        service.register(loginIdFromToken,dto);
        return ResponseEntity.ok("헬스장 일일이용예약 성공");
    }

    @GetMapping("")
    public ResponseEntity<List<GymDailyUseDTO>> getList(@AuthenticationPrincipal String loginIdFromToken){
        return ResponseEntity.ok(service.getList(loginIdFromToken));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOne(@PathVariable("id") Long id){
        service.delete(id);
        return ResponseEntity.ok("삭제 성공");
    }
}
