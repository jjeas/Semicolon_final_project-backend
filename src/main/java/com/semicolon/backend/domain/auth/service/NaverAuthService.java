package com.semicolon.backend.domain.auth.service;

import com.semicolon.backend.domain.auth.dto.TokenResponseDTO;

public interface NaverAuthService {
    public TokenResponseDTO naverLogin(String code);
}
