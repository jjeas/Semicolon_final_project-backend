package com.semicolon.backend.domain.dailyUse;

import com.semicolon.backend.domain.dailyUse.dto.GymDailyUseDTO;
import com.semicolon.backend.domain.dailyUse.service.GymDailyUseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
