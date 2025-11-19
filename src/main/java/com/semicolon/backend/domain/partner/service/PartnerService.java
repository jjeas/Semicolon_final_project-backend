package com.semicolon.backend.domain.partner.service;

import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import com.semicolon.backend.domain.partner.dto.PartnerUploadDTO;
import com.semicolon.backend.domain.partner.entity.PartnerStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PartnerService {
    public ResponseEntity<?> requestPartnerForm(PartnerDTO dto);
    public List<PartnerUploadDTO> getList();
    public PartnerUploadDTO getOne(Long id);
    public void changeStatus(Long id, PartnerStatus status);
}

