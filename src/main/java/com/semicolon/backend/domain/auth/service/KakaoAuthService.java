package com.semicolon.backend.domain.auth.service;

import com.semicolon.backend.domain.auth.dto.TokenResponseDTO;

public interface KakaoAuthService {
    public TokenResponseDTO login(String code);
}
