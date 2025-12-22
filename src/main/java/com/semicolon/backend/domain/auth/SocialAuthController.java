package com.semicolon.backend.domain.auth;


import com.semicolon.backend.domain.auth.dto.KakaoLoginRequestDTO;
import com.semicolon.backend.domain.auth.dto.NaverLoginRequestDTO;
import com.semicolon.backend.domain.auth.dto.TokenResponseDTO;
import com.semicolon.backend.domain.auth.service.KakaoAuthService;
import com.semicolon.backend.domain.auth.service.NaverAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SocialAuthController {

    private final KakaoAuthService kakaoAuthService;
    private final NaverAuthService naverAuthService;

    @PostMapping("/kakao/login")
    public ResponseEntity<TokenResponseDTO> kakaoLogin(@RequestBody KakaoLoginRequestDTO req) {
        TokenResponseDTO token = kakaoAuthService.kakaoLogin(req.getCode());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/naver/login")
    public ResponseEntity<TokenResponseDTO> naverLogin(@RequestBody NaverLoginRequestDTO req) {
        TokenResponseDTO token = naverAuthService.naverLogin(req.getCode());
        return ResponseEntity.ok(token);
    }
}
