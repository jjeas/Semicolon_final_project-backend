package com.semicolon.backend.domain.partner.service;

import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import org.springframework.http.ResponseEntity;

public interface PartnerService {
    public ResponseEntity<?> requestPartnerForm(PartnerDTO dto);
}

