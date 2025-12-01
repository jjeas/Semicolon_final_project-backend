package com.semicolon.backend.domain.facility.repository;

import com.semicolon.backend.domain.facility.entity.FacilitySpace;
import com.semicolon.backend.domain.facility.entity.SpaceRoomType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilitySpaceRepository extends JpaRepository<FacilitySpace, Long> {
    public FacilitySpace findBySpaceRoomType(SpaceRoomType spaceRoomType);

}
