package com.semicolon.backend.domain.support.service;

import com.semicolon.backend.domain.support.dto.SupportDTO;
import com.semicolon.backend.domain.support.dto.SupportResponseDTO;
import com.semicolon.backend.domain.support.dto.SupportUploadDTO;
import com.semicolon.backend.domain.support.entity.Support;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SupportService {
    public ResponseEntity<?> supportReqRegister (String loginIdFromToken, SupportDTO supportDTO);
    public List<SupportUploadDTO> getSupportList (Long id);
    public SupportUploadDTO getOneSupport (Long no);
    public PageResponseDTO<SupportUploadDTO> getSupportAllList (PageRequestDTO pageRequestDTO);
    public List<SupportResponseDTO> registerResponse(Long no, SupportResponseDTO dto);
}
