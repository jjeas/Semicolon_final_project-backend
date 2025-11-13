package com.semicolon.backend.domain.partner.service;

import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PartnerService {
    public ResponseEntity<?> requestPartnerForm(PartnerDTO dto);
}

