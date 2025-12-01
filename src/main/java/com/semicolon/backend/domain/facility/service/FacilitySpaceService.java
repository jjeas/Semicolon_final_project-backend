package com.semicolon.backend.domain.facility.service;

import com.semicolon.backend.domain.facility.dto.FacilitySpaceDTO;

import java.util.List;

public interface FacilitySpaceService{
    public List<FacilitySpaceDTO> findByFacilityId(Long id);
}
