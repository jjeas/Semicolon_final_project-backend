package com.semicolon.backend.domain.partner.service;

import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import com.semicolon.backend.domain.partner.dto.PartnerUploadDTO;
import com.semicolon.backend.domain.partner.entity.PartnerFile;
import com.semicolon.backend.domain.partner.entity.PartnerStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PartnerService {
    public ResponseEntity<?> requestPartnerForm(Long id,PartnerDTO dto);
    public List<PartnerUploadDTO> getList();
    public PartnerUploadDTO getOne(Long id);
    public List<PartnerFile> changeStatus(Long id, PartnerStatus status);
    public void deleteFiles(List<PartnerFile> files);
}

