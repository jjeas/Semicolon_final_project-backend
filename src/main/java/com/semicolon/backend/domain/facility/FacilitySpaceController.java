package com.semicolon.backend.domain.facility;

import com.semicolon.backend.domain.facility.dto.FacilitySpaceDTO;
import com.semicolon.backend.domain.facility.service.FacilitySpaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/facilitySpace")
@RequiredArgsConstructor
public class FacilitySpaceController {

    private final FacilitySpaceService service;

    @GetMapping("/{id}")
    public ResponseEntity<List<FacilitySpaceDTO>> findByFacilityId(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.findByFacilityId(id));
    }

}
