package com.semicolon.backend.domain.facility.dto;

import com.semicolon.backend.domain.facility.entity.FacilityType;
import com.semicolon.backend.domain.facility.entity.SpaceRoomType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FacilitySpaceDTO {

    private Long id;
    private String spaceName;
    private SpaceRoomType spaceRoomType;

}
