package com.semicolon.backend.domain.partner.service;

import com.semicolon.backend.domain.partner.dto.PartnerDTO;
import com.semicolon.backend.domain.partner.dto.PartnerUploadDTO;
import com.semicolon.backend.domain.partner.entity.PartnerFile;
import com.semicolon.backend.domain.partner.entity.PartnerStatus;
import com.semicolon.backend.global.pageable.PageRequestDTO;
import com.semicolon.backend.global.pageable.PageResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PartnerService {

    public PageResponseDTO<PartnerUploadDTO> getList(PageRequestDTO pageRequestDTO);
    public PartnerUploadDTO getOne(Long id);
    public void changeStatus(Long id, PartnerStatus status);
    public void requestPartnerForm(Long id,PartnerDTO dto);
    public List<String> getPartnerClassList(String loginIdFromToken);

}

