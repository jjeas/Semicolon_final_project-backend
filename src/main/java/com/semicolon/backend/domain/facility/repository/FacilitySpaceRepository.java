package com.semicolon.backend.domain.facility.repository;

import com.semicolon.backend.domain.facility.entity.FacilitySpace;
import com.semicolon.backend.domain.facility.entity.SpaceRoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilitySpaceRepository extends JpaRepository<FacilitySpace, Long> {
    public FacilitySpace findBySpaceRoomType(SpaceRoomType spaceRoomType);
    public List<FacilitySpace> findByFacilityId(Long facilityId);

}
