package com.semicolon.backend.domain.facility.service;

import com.semicolon.backend.domain.facility.dto.FacilitySpaceDTO;
import com.semicolon.backend.domain.facility.repository.FacilitySpaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FacilitySpaceServiceImpl implements FacilitySpaceService{

    private final FacilitySpaceRepository repository;
    private final ModelMapper mapper;

    @Override
    public List<FacilitySpaceDTO> findByFacilityId(Long id) {
        return repository.findByFacilityId(id).stream().map(i->mapper.map(i, FacilitySpaceDTO.class)).toList();
    }
}
