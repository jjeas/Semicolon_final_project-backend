package com.semicolon.backend.domain.support.service;

import com.semicolon.backend.domain.support.dto.SupportDTO;
import com.semicolon.backend.domain.support.dto.SupportUploadDTO;
import com.semicolon.backend.domain.support.entity.Support;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SupportService {
    public ResponseEntity<?> supportReqRegister (SupportDTO supportDTO);
    public List<SupportUploadDTO> getSupportList (Long id);
}
