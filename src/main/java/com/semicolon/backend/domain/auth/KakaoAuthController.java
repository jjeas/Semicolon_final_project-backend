package com.semicolon.backend.domain.auth;


import com.semicolon.backend.domain.auth.dto.KakaoLoginRequestDTO;
import com.semicolon.backend.domain.auth.dto.TokenResponseDTO;
import com.semicolon.backend.domain.auth.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/kakao")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody KakaoLoginRequestDTO req) {
        TokenResponseDTO token = kakaoAuthService.login(req.getCode());
        return ResponseEntity.ok(token);
    }
}
